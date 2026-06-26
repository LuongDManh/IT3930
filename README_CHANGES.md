# Tổng hợp thay đổi

## Ca sử dụng 5: Dịch vụ - tiện ích

- Thêm entity `ServiceRequest` để lưu yêu cầu dịch vụ của chủ hộ.
- Thêm DTO, repository, service và API cho owner tạo/xem yêu cầu dịch vụ.
- Thêm API cho admin xem yêu cầu, gán nhân viên và cập nhật trạng thái.
- Khi admin gán nhân viên, hệ thống tự tạo task động (`DYNAMIC`) cho staff.
- Staff có thể bấm hoàn thành task, yêu cầu dịch vụ sẽ chuyển sang trạng thái `DONE`.
- Thêm 2 màn hình mới:
  - `/owner/service-requests`
  - `/admin/service-requests`
- Thêm card truy cập Service Requests trên dashboard của owner và admin.

## Ca sử dụng 3: Thanh toán giả lập

- Thêm nút `Pay Bill` trong trang hóa đơn của owner.
- Thanh toán chỉ là giả lập, không kết nối cổng thanh toán thật.
- Khi bấm thanh toán, bill được đánh dấu đã thanh toán, lưu mã biên lai giả lập và thời gian thanh toán.

## File thay đổi theo từng chức năng

### Lưu yêu cầu dịch vụ

- `src/main/java/com/IT3930/apartment/model/ServiceRequest.java`: thêm entity lưu yêu cầu dịch vụ.
- `src/main/java/com/IT3930/apartment/dto/ServiceRequestDTO.java`: thêm DTO để trả dữ liệu yêu cầu dịch vụ ra giao diện.
- `src/main/java/com/IT3930/apartment/repository/ServiceRequestRepository.java`: thêm repository truy vấn yêu cầu dịch vụ.
- `src/main/java/com/IT3930/apartment/service/ServiceRequestService.java`: xử lý tạo yêu cầu, gán staff, cập nhật trạng thái và đồng bộ với task.

### API cho owner, admin, staff

- `src/main/java/com/IT3930/apartment/controller/OwnerController.java`: thêm API owner tạo/xem service request và API thanh toán giả lập bill.
- `src/main/java/com/IT3930/apartment/controller/AdminController.java`: thêm API admin xem service request, gán staff và cập nhật trạng thái.
- `src/main/java/com/IT3930/apartment/controller/StaffController.java`: thêm API staff đánh dấu task hoàn thành.
- `src/main/java/com/IT3930/apartment/controller/DashboardController.java`: thêm route cho trang service request của owner/admin.

### Giao diện

- `src/main/resources/templates/owner_service_requests.html`: thêm trang owner tạo và theo dõi yêu cầu dịch vụ.
- `src/main/resources/templates/admin_service_requests.html`: thêm trang admin quản lý, gán staff và cập nhật trạng thái yêu cầu.
- `src/main/resources/templates/dashboard.html`: thêm card Service Requests cho owner và admin.
- `src/main/resources/templates/staff_tasks.html`: thêm nút hoàn thành task cho staff.
- `src/main/resources/templates/owner_bills.html`: thêm nút `Pay Bill` và hiển thị mã biên lai giả lập.

### Thanh toán giả lập

- `src/main/java/com/IT3930/apartment/model/bill/Bill.java`: thêm trường `paymentReference` và `paidAt`.
- `src/main/java/com/IT3930/apartment/service/BillService.java`: thêm logic fake payment, đánh dấu bill đã thanh toán và sinh mã biên lai.

### Cấu hình an toàn

- `.gitignore`: thêm `application.properties` để tránh commit file cấu hình có thông tin nhạy cảm.

## Lưu ý chạy project

- Project đang dùng PostgreSQL từ file `application.properties` đặt ở workspace.
- File `application.properties` có thông tin nhạy cảm nên đã được đưa vào `.gitignore`, không nên commit lên repo.
- Lệnh đã kiểm tra build:

```bash
./mvnw -DskipTests package
```

## Tài khoản demo đã tạo

- Admin: `demo.admin@apartment.com` / `123456`
- Owner: `demo.owner@apartment.com` / `123456`
- Staff: `demo.staff@apartment.com` / `123456`

Owner demo đang được gán căn hộ `103`, tầng `1`.
