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
                <a class="nav-link" href="SV/">
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
                                        <div class="text-truncate">Hi there! I am wondering if you can help me with a
                                            problem I've been having.</div>
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
                    <div class="row">
                    	<div class="col-xl-4 col-lg-5">
                    		<div class="card border-left-primary shadow h-100 py-2">
                    			<div class="card-body">                    				
                    				<img src="sources/img/undraw_profile.svg">
                    			</div>
                    		</div>
                    	</div>
                    	
                    	<div class="col-xl-8 col-lg-6">
                    		<div class="card border-left-primary shadow h-100 py-2">
                    			<div class="card-body">
                    				<form class="user" action="SV/${chedo}.html" method="post">
                    					<div class="form-group">
                                            <input type="text" class="form-control form-control-user"
                                                id="input_manv" aria-describedby="textHelp" name="masv"
                                                placeholder="Mã nhân viên..." value="${sv.MASV}" required>
                                        </div>
                                        <div class="form-group">
                                            <input type="text" class="form-control form-control-user"
                                                id="input_hoten" aria-describedby="textHelp" name="hoten"
                                                placeholder="Họ tên..." value="${sv.HOTEN}" required>
                                        </div>
                                        <div class="form-group">
                                            <input type="date" class="form-control form-control-user"
                                                id="input_date" aria-describedby="emailHelp" name="ngaysinh"
                                                placeholder="Ngày sinh..." value="${sv.NGAYSINH}" required>
                                        </div>
                                        <div class="form-group">
                                            <input type="text" class="form-control form-control-user"
                                                id="input_luong" aria-describedby="textHelp" name="diachi" 
                                                placeholder="Địa chỉ..." value="${sv.DIACHI}" required>
                                        </div>
                                        <div class="form-group">
                                        	<select class="form-control" style="border-radius: 10rem;"
                                        	id="sel_lop" name="malop" required="required">
                                        		<option disabled="disabled" ${sv.lop.MALOP!=null?'':'selected'}>Lớp...</option>
                                        		<c:forEach items="${account.lops}" var="lop">
                                        			<option value="${lop.MALOP}" ${sv.lop.MALOP==lop.MALOP?'selected':''}>
                                        			${lop.MALOP} - ${lop.TENLOP}</option>
                                        		</c:forEach>
                                        	</select>
                                        </div>
                                        <div class="form-group">
                                            <input type="text" class="form-control form-control-user"
                                                id="input_tendn" aria-describedby="textHelp" name="tendn" 
                                                placeholder="Tên đăng nhập..." value="${sv.TENDN}" required>
                                        </div>
                                        <div class="form-group">
                                            <input type="password" class="form-control form-control-user"
                                                id="input_password1" aria-describedby="passwordHelp" name="matkhau1" 
                                                placeholder="Mật khẩu mới..." required>
                                        </div>
                                        <div class="form-group">
                                            <input type="password" class="form-control form-control-user"
                                                id="input_password2" aria-describedby="passwordHelp" name="matkhau2" 
                                                placeholder="Nhập lại mật khẩu..." required>
                                        </div>
                                        <button type="submit" class="btn btn-primary btn-user btn-block">
                                            ${chedo=='insert'?'Thêm sinh viên':'Chỉnh sửa'}
                                        </button>
                    				</form>
                    			</div>
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
                        <span>Copyright &copy; Your Website 2020</span>
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
                <div class="modal-body">Chọn "Logout" nếu bạn muốn đăng xuất.</div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <a class="btn btn-primary" href="logout.html">Logout</a>
                </div>
            </div>
        </div>
    </div>

<jsp:include page="../footer.jsp"></jsp:include>