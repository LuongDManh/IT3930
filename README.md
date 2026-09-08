# Hệ Thống Quản Lý Chung Cư

Ứng dụng web Spring Boot phục vụ quản lý chung cư, được triển khai trên cụm **Docker Swarm 3 node** với tự động hoá CI/CD đầy đủ và giám sát bằng Prometheus.

---

## Mục Lục

- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [CI — Tích hợp liên tục](#ci--tích-hợp-liên-tục)
- [CD — Triển khai liên tục](#cd--triển-khai-liên-tục)
- [Triển khai — Docker Swarm](#triển-khai--docker-swarm)
- [Bộ giám sát (Monitoring Stack)](#bộ-giám-sát-monitoring-stack)
- [Danh sách GitHub Secrets](#danh-sách-github-secrets)
- [URL truy cập](#url-truy-cập)

---

## Công nghệ sử dụng

| Tầng | Công nghệ |
|---|---|
| Backend | Spring Boot 4.1 (Java 21) |
| Cơ sở dữ liệu | PostgreSQL (Aiven cloud) |
| Template engine | Thymeleaf |
| Bảo mật | Spring Security |
| Gửi email | Spring Mail (Gmail SMTP) |
| Container hoá | Docker + Docker Swarm |
| CI/CD | GitHub Actions |
| Thu thập metrics | Prometheus |
| Giám sát host | Node Exporter |
| Giám sát container | cAdvisor |
| Trực quan hoá | Grafana |

---

## Cấu trúc dự án

```
.
├── src/                            # Mã nguồn Spring Boot
├── Dockerfile                      # Định nghĩa image container
├── docker-compose.yml              # Định nghĩa toàn bộ Swarm stack
├── create-configs.sh               # Tạo Docker configs trước lần deploy đầu tiên
├── monitoring/
│   ├── prometheus/
│   │   └── prometheus.yml          # Cấu hình scrape của Prometheus
│   └── grafana/
│       └── provisioning/
│           ├── datasources/
│           │   └── prometheus.yml  # Tự động kết nối Grafana → Prometheus
│           └── dashboards/
│               └── dashboards.yml  # Cấu hình provider dashboard
└── .github/
    └── workflows/
        ├── ci.yml                  # CI: build & test mỗi khi push
        └── cd.yml                  # CD: deploy lên Swarm khi merge vào main
```

---

## CI — Tích hợp liên tục

**File:** `.github/workflows/ci.yml`  
**Kích hoạt:** Mỗi lần push hoặc pull request trên bất kỳ nhánh nào

### Các bước thực hiện

| Bước | Hành động |
|---|---|
| Checkout | Tải mã nguồn về runner |
| Cài JDK 21 | Cài Temurin Java 21, cache Maven |
| Chạy test | `./mvnw test` với thông tin DB lấy từ secrets |
| Upload report | Kết quả Surefire test lưu 14 ngày |

CI chạy trên **mọi nhánh** để phát hiện lỗi trước khi merge vào `main`.

---

## CD — Triển khai liên tục

**File:** `.github/workflows/cd.yml`  
**Kích hoạt:** Tự động sau khi CI pass trên nhánh `main`

### Luồng thực hiện

```
CI pass trên nhánh main
         │
         ▼
SSH vào manager VPS ($SWARM_MANAGER_IP)
         │
         ├── git pull (hoặc clone lần đầu)
         ├── ./create-configs.sh   ← tạo lại Docker configs
         ├── docker stack deploy apartment
         └── kiểm tra sức khoẻ (pipeline thất bại nếu có task crash)
```

### Secrets cần thiết

Tất cả giá trị lưu ở **GitHub Secrets** (repo → Settings → Secrets → Actions):

| Secret | Mục đích |
|---|---|
| `SWARM_MANAGER_IP` | IP công khai của node manager trên VPS |
| `SWARM_SSH_USER` | Tên đăng nhập SSH (ví dụ: `ubuntu`) |
| `SWARM_SSH_KEY` | Nội dung đầy đủ private key (định dạng PEM) |
| `DB_URL` | JDBC URL kết nối PostgreSQL |
| `DB_USERNAME` | Tên người dùng database |
| `DB_PASSWORD` | Mật khẩu database |
| `MAIL_USERNAME` | Địa chỉ Gmail dùng gửi mail |
| `MAIL_PASSWORD` | Mật khẩu ứng dụng Gmail |
| `GRAFANA_PASSWORD` | Mật khẩu admin Grafana |

---

## Triển khai — Docker Swarm

### Sơ đồ cụm

3 VPS được kết nối thành cụm Docker Swarm (1 manager, 2 worker).

```
Node 1 — manager          Node 2 — worker           Node 3 — worker
─────────────────         ─────────────────          ─────────────────
app (replica 1)           app (replica 2)            app (replica 3)
node-exporter             node-exporter              node-exporter
cadvisor                  cadvisor                   cadvisor
prometheus (cố định)
grafana (cố định)
```

### Các service trong `docker-compose.yml`

| Service | Image | Số lượng | Mục đích |
|---|---|---|---|
| `app` | `luongdmanh/apartment-web-app:ver1` | 3 (mỗi node 1) | Ứng dụng chính |
| `prometheus` | `prom/prometheus:latest` | 1 (manager) | Lưu trữ metrics |
| `node-exporter` | `prom/node-exporter:latest` | global (tất cả node) | Metrics máy chủ |
| `cadvisor` | `gcr.io/cadvisor/cadvisor:latest` | global (tất cả node) | Metrics container |
| `grafana` | `grafana/grafana:latest` | 1 (manager) | Giao diện dashboard |

Tất cả service dùng chung một **overlay network** (`monitoring`) trải rộng trên cả 3 host, cho phép mọi container giao tiếp với nhau qua tên service.

### Triển khai lần đầu (chạy trên node manager)

```bash
# 1. Khởi tạo Swarm (trên manager)
docker swarm init
# Sao chép token in ra, sau đó chạy lệnh sau trên mỗi worker:
# docker swarm join --token <token> <manager-ip>:2377

# 2. Clone repo về manager
git clone https://github.com/<your-org>/IT3930 ~/apartment
cd ~/apartment

# 3. Tạo Docker configs (bắt buộc chạy trước stack deploy)
bash create-configs.sh

# 4. Deploy stack
docker stack deploy -c docker-compose.yml apartment

# 5. Kiểm tra trạng thái
docker stack ps apartment
```

### Cập nhật Docker configs (khi thay đổi file monitoring)

Docker configs là bất biến — muốn cập nhật phải xoá và tạo lại:

```bash
# Xoá config cũ và tạo lại
bash create-configs.sh

# Buộc khởi động lại service bị ảnh hưởng
docker service update --force apartment_prometheus
```

---

## Bộ giám sát (Monitoring Stack)

### Kiến trúc

```
Metrics máy chủ ──▶ node-exporter:9100 ──┐
                    (1 trên mỗi node)      │
                                           ├──▶ Prometheus:9090 ──▶ Grafana:3000
Metrics container ──▶ cadvisor:8080 ──────┘
                      (1 trên mỗi node)
```

### Các thành phần

#### Prometheus (`prom/prometheus`)
- **Port:** `9090`
- **Vai trò:** Cơ sở dữ liệu chuỗi thời gian trung tâm. Tự động thu thập (scrape) metrics từ Node Exporter và cAdvisor mỗi 15 giây. Lưu dữ liệu trong 15 ngày.
- **Config:** `monitoring/prometheus/prometheus.yml`
- **Điểm quan trọng:** Dùng `dns_sd_configs` với DNS `tasks.<service>` — Docker Swarm phân giải tên này thành IP riêng của từng task, do đó Prometheus scrape **cả 3 node** một cách riêng biệt, không phải chỉ một node ngẫu nhiên qua VIP.

#### Node Exporter (`prom/node-exporter`)
- **Port:** `9100` (host mode — mỗi node dùng port riêng của mình)
- **Vai trò:** Thu thập **metrics cấp máy chủ** — CPU, RAM, disk I/O, filesystem, lưu lượng mạng, system load. Chạy ở `mode: global` nên tự động có mặt trên mọi node.
- **Mount:** `/proc`, `/sys`, `/` từ host (chỉ đọc)

#### cAdvisor (`gcr.io/cadvisor/cadvisor`)
- **Port:** `8081` → `8080` (host mode)
- **Vai trò:** Thu thập **metrics cấp container** — CPU, RAM, mạng, disk theo từng container. Chạy ở `mode: global` nên theo dõi container trên tất cả các node.
- **Mount:** Docker socket và filesystem host (chỉ đọc)

#### Grafana (`grafana/grafana`)
- **Port:** `3000`
- **Vai trò:** Giao diện dashboard trực quan. Được tự động cấu hình Prometheus làm data source mặc định.
- **Đăng nhập:** `admin` / giá trị secret `GRAFANA_PASSWORD` (mặc định: `admin`)
- **Dashboard khuyên dùng (import theo ID):**

  | Dashboard ID | Tên | Giám sát |
  |---|---|---|
  | `1860` | Node Exporter Full | CPU, RAM, disk, mạng của host |
  | `14282` | Docker cAdvisor | Tài nguyên theo từng container |

  > Grafana → Dashboards → Import → nhập ID → Load

### Giải thích cấu hình scrape của Prometheus

```yaml
# tasks.node-exporter phân giải thành IP của cả 3 task (không phải VIP)
- job_name: "node_exporter"
  dns_sd_configs:
    - names: ["tasks.node-exporter"]
      type: A
      port: 9100

# tasks.cadvisor phân giải thành IP của cả 3 task (không phải VIP)
- job_name: "cadvisor"
  dns_sd_configs:
    - names: ["tasks.cadvisor"]
      type: A
      port: 8080
```

Nếu dùng tên service thông thường (`node-exporter:9100`), Swarm sẽ định tuyến qua **VIP** và chỉ chạm vào một task ngẫu nhiên. Dùng `tasks.<service>` bỏ qua VIP, trả về tất cả IP của các task, cho phép Prometheus có một scrape target riêng cho mỗi node.

---

## Danh sách GitHub Secrets

| Secret | Dùng bởi | Mô tả |
|---|---|---|
| `SWARM_MANAGER_IP` | CD | IP công khai của VPS manager |
| `SWARM_SSH_USER` | CD | Tên đăng nhập SSH trên manager |
| `SWARM_SSH_KEY` | CD | Nội dung đầy đủ private key PEM |
| `DB_URL` | CI + CD | JDBC URL kết nối PostgreSQL |
| `DB_USERNAME` | CI + CD | Tên người dùng database |
| `DB_PASSWORD` | CI + CD | Mật khẩu database |
| `MAIL_USERNAME` | CD | Địa chỉ Gmail gửi mail |
| `MAIL_PASSWORD` | CD | Mật khẩu ứng dụng Gmail |
| `GRAFANA_PASSWORD` | CD | Mật khẩu admin giao diện Grafana |

---

## URL truy cập

| Service | URL | Ghi chú |
|---|---|---|
| Ứng dụng | `http://<ip-bất-kỳ-node>:8080` | Swarm tự cân bằng tải qua 3 replica |
| Prometheus | `http://<ip-manager>:9090` | Truy vấn metrics & xem trạng thái target |
| Grafana | `http://<ip-manager>:3000` | Giao diện dashboard |
| Node Exporter | `http://<ip-node>:9100/metrics` | Metrics thô của máy chủ theo từng node |
| cAdvisor | `http://<ip-node>:8081` | Metrics thô của container theo từng node |
