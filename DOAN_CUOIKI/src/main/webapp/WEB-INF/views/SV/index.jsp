<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="f"%>
<jsp:include page="../header.jsp"></jsp:include>

	<!-- Page Wrapper -->
    <div id="wrapper">

        <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Side bar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.html">
                <div class="sidebar-brand-icon rotate-n-15">
                    <i class="fas fa-laugh-wink"></i>
                </div>
                <div class="sidebar-brand-text mx-3">QLSV</div>
            </a>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Các nút menu -->
            <li class="nav-item">
                <a class="nav-link" href="nhanvien/">
                    <i class="fas fa-fw fa-table"></i>
                    <span>Nhân viên</span></a>
            </li>
            
			<hr class="sidebar-divider">
			
            <li class="nav-item">
                <a class="nav-link" href="lop/">
                    <i class="fas fa-fw fa-table"></i>
                    <span>Lớp học</span></a>
            </li>
			
			<hr class="sidebar-divider">
			
			<li class="nav-item">
                <a class="nav-link" href="SV/#">
                    <i class="fas fa-fw fa-table"></i>
                    <span>Sinh viên</span></a>
            </li>
            
            <hr class="sidebar-divider">
            
            <li class="nav-item">
                <a class="nav-link" href="hocphan/">
                    <i class="fas fa-fw fa-table"></i>
                    <span>Học phần</span></a>
            </li>
            
            <hr class="sidebar-divider">
            
            <li class="nav-item">
                <a class="nav-link" href="bangdiem/">
                    <i class="fas fa-fw fa-table"></i>
                    <span>Bảng điểm</span></a>
            </li>
            
            <!-- Divider -->
            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>

        </ul>
        <!-- End of Side bar -->

        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">

            <!-- Main Content -->
            <div id="content">

                <!-- Top bar -->
                <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                    <!-- Side bar Toggle (Top bar) -->
                    <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                        <i class="fa fa-bars"></i>
                    </button>

                    <!-- Top bar Search -->
                    <form action="search.html" method="get"
                        class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
                        <div class="input-group">
                            <input type="text" class="form-control bg-light border-0 small" placeholder="Tìm kiếm..."
                                aria-label="Search" aria-describedby="basic-addon2" name="input">
                            <div class="input-group-append">
                                <button class="btn btn-primary" type="button">
                                    <i class="fas fa-search fa-sm"></i>
                                </button>
                            </div>
                        </div>
                    </form>

                    <!-- Top bar Nav bar -->
                    <ul class="navbar-nav ml-auto">

                        <!-- Nav Item - Search Drop down (Visible Only XS) -->
                        <li class="nav-item dropdown no-arrow d-sm-none">
                            <a class="nav-link dropdown-toggle" href="#" id="searchDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-search fa-fw"></i>
                            </a>
                            <!-- Drop down - Messages -->
                            <div class="dropdown-menu dropdown-menu-right p-3 shadow animated--grow-in"
                                aria-labelledby="searchDropdown">
                                <form class="form-inline mr-auto w-100 navbar-search" action = "search.html" method="get">
                                    <div class="input-group">
                                        <input type="text" class="form-control bg-light border-0 small" name="input"
                                            placeholder="Tìm kiếm..." aria-label="Search"
                                            aria-describedby="basic-addon2">
                                        <div class="input-group-append">
                                            <button class="btn btn-primary" type="button">
                                                <i class="fas fa-search fa-sm"></i>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </li>

                        <!-- Nav Item - Messages -->
                        <li class="nav-item dropdown no-arrow mx-1">
                            <a class="nav-link dropdown-toggle" href="#" id="messagesDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-envelope fa-fw"></i>
                                <!-- Counter - Messages -->
                                <c:if test ="${thongbao!=null}">
                                <span class="badge badge-danger badge-counter">${thongbao.count}</span>
                                </c:if>
                            </a>
                            <!-- Drop down - Messages -->
                            <div class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in"
                                aria-labelledby="messagesDropdown">
                                <h6 class="dropdown-header">
                                    Message Center
                                </h6>
                                <c:if test="${thongbao!=null}">
                                <a class="dropdown-item d-flex align-items-center" href="#">
                                    <div class="dropdown-list-image mr-3">
                                        <img class="rounded-circle" src="sources/img/undraw_profile.svg"
                                            alt="...">
                                    </div>
                                    <div class="font-weight-bold">
                                        <div class="text-truncate">${thongbao.noidung}</div>
                                    </div>
                                </a>
                                </c:if>
                            </div>
                        </li>

                        <div class="topbar-divider d-none d-sm-block"></div>

                        <!-- Nav Item - User Information -->
                        <li class="nav-item dropdown no-arrow">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <span class="mr-2 d-none d-lg-inline text-gray-600 small">${account.TENDN}</span>
                                <img class="img-profile rounded-circle"
                                    src="sources/img/undraw_profile.svg">
                            </a>
                            <!-- Drop down - User Information -->
                            <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in"
                                aria-labelledby="userDropdown">
                                <a class="dropdown-item" href="index.html">
                                    <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Profile
                                </a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                    <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Logout
                                </a>
                            </div>
                        </li>

                    </ul>

                </nav>
                <!-- End of Top bar -->

                <!-- Begin Page Content -->
                <div class="container-fluid">
					<c:if test="${loi!=null}">
                      	<div class="alert alert-danger" role="alert">
						  ${loi}
						</div>
                    </c:if>
                    <c:if test="${thanhcong!=null}">
                      	<div class="alert primary-danger" role="alert">
						  ${thanhcong}
						</div>
                    </c:if>
                    <!-- Page Heading -->
                    <h1 class="h3 mb-4 text-gray-800">THÔNG TIN SINH VIÊN</h1>
					<!-- Content Row -->
					<div class="col-xl-9 col-lg-7">
					<div class="card shadow mb-4">
						<form class="input-group" action="SV/index.html" method="get">
							<select class="form-control" name="malop">
								<option disabled="disabled" ${malop!=null?'':'selected'}>Chọn lớp...</option>
								<c:forEach items="${account.lops}" var="lop">
									<option value="${lop.MALOP}" ${malop==lop.MALOP?'selected':''}>
									${lop.MALOP} - ${lop.TENLOP}</option>
								</c:forEach>
							</select>
							<button class="btn btn-primary">Lọc</button>
						</form>
					</div>
					</div>
                    <!-- DataTales Example -->
                    
                    <div class="card shadow mb-4">
                        
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">Bảng thông tin sinh viên</h6>
                        </div>                        
                        <div class="card-body">
                        	<div class="table-responsive">
                        		<table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                        			<thead>
                                        <tr>
                                            <th>Mã sinh viên</th>
                                            <th>Họ tên</th>
                                            <th>Ngày sinh</th>
                                            <th>Địa chỉ</th>
                                            <th>Mã lớp</th>
                                            <th>Tên lớp</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${sinhviens}" var="sv">
	                                	<tr>
	                                    	<td>${sv.MASV}</td>
	                                        <td>${sv.HOTEN}</td>
	                                        <td>${sv.NGAYSINH}</td>
	                                        <td>${sv.DIACHI}</td>
	                                        <td>${sv.lop.MALOP}</td>
	                                        <td>${sv.lop.TENLOP}</td>
	                                        <td>
								            <a class="btn btn-primary" href="SV/update.html?masv=${sv.MASV}">
								                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">
  <path d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5v11z"/>
</svg>
								                <span>Chỉnh sửa</span></a>
								            <a class="btn btn-danger" onclick="deleteModal('${sv.MASV}')"
								            data-toggle="modal" data-target="#deleteModal">
								            	<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash2" viewBox="0 0 16 16">
  <path d="M14 3a.702.702 0 0 1-.037.225l-1.684 10.104A2 2 0 0 1 10.305 15H5.694a2 2 0 0 1-1.973-1.671L2.037 3.225A.703.703 0 0 1 2 3c0-1.105 2.686-2 6-2s6 .895 6 2zM3.215 4.207l1.493 8.957a1 1 0 0 0 .986.836h4.612a1 1 0 0 0 .986-.836l1.493-8.957C11.69 4.689 9.954 5 8 5c-1.954 0-3.69-.311-4.785-.793z"/>
</svg>							<!-- Ý tưởng là tạo n cái modal có tên là deleteModal_manv, khi bấm nó sẽ tìm data-target để mở -->
								            	<span>Xóa</span></a>
	                                        </td>
	                                        </tr>
	                                </c:forEach>
                                   	</tbody>
                        		</table>
                        		<a class="btn btn-primary" href="SV/insert.html">
								                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-square" viewBox="0 0 16 16">
  <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>
  <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
</svg>
								                <span>Thêm sinh viên</span></a>
                        	</div>
                        </div>                       
                    </div>                   
                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- End of Main Content -->

            <!-- Footer -->
            <footer class="sticky-footer bg-white">
                <div class="container my-auto">
                    <div class="copyright text-center my-auto">
                        <span>Copyright &copy; Nhóm 3 môn An toàn ứng dụng Web & CSDL 2021</span>
                    </div>
                </div>
            </footer>
            <!-- End of Footer -->

        </div>
        <!-- End of Content Wrapper -->

    </div>
    <!-- End of Page Wrapper -->

    <!-- Scroll to Top Button-->
    <a class="scroll-to-top rounded" href="#page-top">
        <i class="fas fa-angle-up"></i>
    </a>

    <!-- Logout Modal-->
    <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Bạn muốn đăng xuất?</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">Chọn Logout Nếu bạn muốn đăng xuất</div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <a class="btn btn-primary" href="logout.html">Logout</a>
                </div>
            </div>
        </div>
    </div>
    <!-- Delete Modal-->
    <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Xóa sinh viên</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <form action="SV/delete.html" method="post">
                <div class="modal-body">Bạn thực sự muốn xóa sinh viên này?</div>
                <input name="masv" id="del_masv" hidden="hidden" required="required">
                <div class="modal-footer">
                    <button class="btn btn-primary" id="delete" type="submit">Xóa</button>
                </div>
                </form>
            </div>
        </div>
    </div>
    <script>
    function deleteModal(masv){
    	document.getElementById('del_masv').value=masv;
    }
    </script>
<jsp:include page="../footer.jsp"></jsp:include>