# account-dept (Spring Boot)

Backend quản lý Accounts & Departments (Spring Boot 3.3, MySQL, JWT, Swagger).

## Chạy nhanh
1. Tạo database MySQL `account_dept_db`.
2. Đặt biến môi trường (hoặc dùng giá trị mặc định trong `application.yml`):
   `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` (>= 32 ký tự), `DB_URL`, `CORS_ALLOWED_ORIGIN`
3. Chạy:
```bash
mvn spring-boot:run
```
- Port: `8081`
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- Lần chạy đầu sẽ tự tạo dữ liệu mẫu: `admin / 123456` (ADMIN) và `user2..user30 / 123456`.

## API
- `POST /api/auth/login`, `/register`
- `POST /api/auth/forgot-password` (body: `{"email": "..."}`) -> token reset được ghi vào log (demo, chưa gửi email)
- `POST /api/auth/reset-password` (body: `{"token": "...", "newPassword": "..."}`)
- `POST /api/auth/change-password` (cần Bearer token)
- `/api/accounts`, `/api/departments` (ADMIN: toàn quyền; MANAGER: chỉ xem (GET); header `Authorization: Bearer <token>`)
  - `GET /api/accounts` hỗ trợ phân trang, sort, search, lọc theo role/department/ngày tạo
  - `DELETE /api/accounts` xóa nhiều (body: danh sách id)
