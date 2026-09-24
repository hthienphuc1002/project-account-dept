# Account & Department Management System

Backend REST API quản lý **tài khoản (Account)** và **phòng ban (Department)**, xây dựng bằng Spring Boot và MySQL.

## Công nghệ sử dụng

- Java 17, Spring Boot 3.3
- Spring Data JPA (Hibernate), MySQL
- Spring Security + JWT (xác thực và phân quyền)
- Swagger / OpenAPI (springdoc) để tài liệu hóa và thử API
- ModelMapper, Lombok, Log4j2

## Tính năng chính

- Đăng ký, đăng nhập bằng JWT, đổi mật khẩu, quên và đặt lại mật khẩu
- CRUD tài khoản và phòng ban
- Tìm kiếm, lọc (theo vai trò, phòng ban, ngày tạo), sắp xếp và phân trang
- Xóa nhiều tài khoản cùng lúc
- Phân quyền theo vai trò:

| Vai trò | Quyền |
|---|---|
| ADMIN | Toàn quyền (xem, thêm, sửa, xóa) |
| MANAGER | Chỉ xem (GET) tài khoản và phòng ban |
| EMPLOYEE | Không truy cập API quản lý |

- Xử lý lỗi tập trung, kiểm tra dữ liệu đầu vào (validation)

## Yêu cầu môi trường

- JDK 17
- Maven 3.8+ (hoặc chạy bằng Spring Tool Suite / IntelliJ)
- MySQL 8

## Cách chạy

1. Clone project:
```bash
   git clone https://github.com/hthienphuc1002/project-account-dept.git
   cd project-account-dept
```
2. Tạo database trong MySQL:
```sql
   CREATE DATABASE account_dept_db;
```
3. Đặt biến môi trường (hoặc dùng giá trị mặc định trong `application.yml`):

   | Biến | Ý nghĩa |
   |---|---|
   | `DB_USERNAME` | Tài khoản MySQL (mặc định `root`) |
   | `DB_PASSWORD` | Mật khẩu MySQL |
   | `DB_URL` | Địa chỉ kết nối MySQL |
   | `JWT_SECRET` | Khóa bí mật JWT (từ 32 ký tự trở lên) |
   | `CORS_ALLOWED_ORIGIN` | Các origin được phép, cách nhau bằng dấu phẩy |

4. Chạy ứng dụng:
```bash
   mvn spring-boot:run
```

- Port: `8081`
- Swagger UI: http://localhost:8081/swagger-ui/index.html
- Lần chạy đầu tiên, ứng dụng tự tạo dữ liệu mẫu (3 phòng ban, 30 tài khoản).

## Tài khoản demo

| Username | Mật khẩu | Vai trò |
|---|---|---|
| `admin` | `123456` | ADMIN |
| `user5` | `123456` | MANAGER |
| `user2` | `123456` | EMPLOYEE |

## Hướng dẫn thử API bằng Swagger

1. Mở Swagger UI, chọn `POST /api/auth/login`, bấm **Try it out** và nhập:
```json
   { "username": "admin", "password": "123456" }
```
2. Copy giá trị `token` trong kết quả (không lấy dấu nháy).
3. Bấm nút **Authorize** ở đầu trang, dán token vào ô Value rồi bấm **Authorize**.
4. Thử các API bên dưới.

## Danh sách API

### Xác thực (`/api/auth`)

| Method | Đường dẫn | Mô tả |
|---|---|---|
| POST | `/api/auth/login` | Đăng nhập, trả về JWT |
| POST | `/api/auth/register` | Đăng ký tài khoản mới |
| POST | `/api/auth/forgot-password` | Yêu cầu đặt lại mật khẩu (token ghi vào log, chưa gửi email) |
| POST | `/api/auth/reset-password` | Đặt lại mật khẩu bằng token |
| POST | `/api/auth/change-password` | Đổi mật khẩu (cần đăng nhập) |

### Tài khoản (`/api/accounts`)

| Method | Đường dẫn | Mô tả |
|---|---|---|
| GET | `/api/accounts` | Danh sách, hỗ trợ `search`, `role`, `departmentId`, `createdFrom`, `createdTo`, `page`, `size`, `sort` |
| GET | `/api/accounts/{id}` | Chi tiết một tài khoản |
| POST | `/api/accounts` | Tạo tài khoản (ADMIN) |
| PUT | `/api/accounts/{id}` | Cập nhật tài khoản (ADMIN) |
| DELETE | `/api/accounts` | Xóa nhiều tài khoản, body là danh sách id (ADMIN) |

### Phòng ban (`/api/departments`)

| Method | Đường dẫn | Mô tả |
|---|---|---|
| GET | `/api/departments` | Danh sách, hỗ trợ `search`, `type`, `createdFrom`, `createdTo`, `page`, `size`, `sort` |
| GET | `/api/departments/{id}` | Chi tiết một phòng ban |
| POST | `/api/departments` | Tạo phòng ban (ADMIN) |
| PUT | `/api/departments/{id}` | Cập nhật phòng ban (ADMIN) |
| DELETE | `/api/departments/{id}` | Xóa phòng ban, bị chặn nếu còn nhân viên (ADMIN) |

## Cấu trúc project

```
src/main/java/com/example/accountdept
├── config       # CORS, Swagger, ModelMapper, dữ liệu mẫu
├── controller   # REST controllers
├── dto          # Đối tượng truyền dữ liệu
├── entity       # JPA entities
├── exception    # Xử lý lỗi tập trung
├── repository   # Spring Data repositories
├── security     # JWT, cấu hình Spring Security
├── service      # Business logic
└── spec         # JPA Specifications (tìm kiếm, lọc)
```

## Tác giả

hthienphuc1002
