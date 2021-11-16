# DOAN_ATUDWEB-CSDL_HKI_NAM4
## QUẢN LÍ SINH VIÊN ĐƠN GIẢN
Công nghệ sử dụng: Java Spring MVC + SQL Server
### Setup SQL
Chạy file QLSV3.sql
### Setup Project
Cách thứ nhất: Sử dụng eclipse SE
1. Thêm Server Apache Tomcat 8.5
2. Mở project DOAN_CUOIKI bằng eclipse
3. Chỉnh sửa user và pass SQL Server trong đường dẫn src\main\webapp\WEB-INF\configs\spring-config-hibernate.xml
4. Chạy Server trên eclipse
5. User/pass đăng nhập là NVA/123456

Cách thứ hai: Setup Server Apache Tomcat 8.5
Tham khảo thêm tại [https://openplanning.net/11583/cai-dat-tomcat-server]

###Lưu ý
Trước khi chạy Server cần tìm nơi để chứa thư mục rsa_keypair
Vì mỗi máy sẽ có nơi chưa thư mục này khác nhau
Build riêng GenerateKeys.java và chạy để tìm đường dẫn đúng và đặt thư mục rsa_keypair vào
