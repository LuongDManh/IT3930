# Tóm tắt thay đổi

Đây là bản tổng hợp cuối, gồm cả commit `f694ca2` và phần bổ sung sau đó.

- **Ca 2 - Căn hộ:** Admin thêm, sửa, xóa căn hộ và xuất danh sách CSV.
  - `ApartmentController`, `ApartmentService`, `manage_apartments.html`.
- **Ca 3 - Hóa đơn:** Owner thanh toán giả lập; hệ thống đánh dấu đã thanh toán, lưu mã tham chiếu và thời gian; bổ sung hạn thanh toán và tải biên lai.
  - `Bill`, `BillGenerationRequestDTO`, `BillService`, `AdminController`, `OwnerController`, `admin_bills.html`, `owner_bills.html`.
- **Ca 5 - Dịch vụ:** Admin quản lý danh mục dịch vụ; Owner chọn dịch vụ để tạo/theo dõi yêu cầu; Admin gán Staff và cập nhật trạng thái; hệ thống tự tạo nhiệm vụ phát sinh; Staff xác nhận hoàn thành.
  - `Amenity`, `ServiceRequest` cùng DTO, repository, service, controller và các trang `admin_service_requests.html`, `owner_service_requests.html`, `staff_tasks.html`, `dashboard.html`.

## Chạy thử

- Build: `./mvnw -DskipTests package`.
- File cấu hình và dữ liệu kiểm thử local không được đưa lên Git.
