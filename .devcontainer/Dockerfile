# Sử dụng official image debian:trixie-slim
FROM debian:trixie-slim

# Cài đặt các công cụ cần thiết (thêm ca-certificates, git, curl cho DevContainer) và Temurin JDK 21
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get install -y wget apt-transport-https gpg ca-certificates git curl \
    && wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor | tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null \
    && echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list \
    && apt-get update \
    && apt-get install -y temurin-21-jdk maven \
    # Dọn dẹp cache của apt để giảm dung lượng image
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*