<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="f"%>
<jsp:include page="header.jsp"></jsp:include>
    <div class="container">
		
        <!-- Outer Row -->
        <div class="row justify-content-center">
			
            <div class="col-xl-10 col-lg-12 col-md-9">
            	
				<c:if test="${tb!=null}">
					<div class="alert alert-danger" role="alert">${tb}</div>
				</c:if>
				
                <div class="card o-hidden border-0 shadow-lg my-5">
                    <div class="card-body p-0">
                        <!-- Nested Row within Card Body -->
                        <div class="row">
                            <div class="col-lg-6 d-none d-lg-block" 
                            	style = "background: url('sources/images/ptit3.jpg');
                            	background-position: center;
                            	background-size: cover;"></div>                            
                            <div class="col-lg-6">
                                <div class="p-5">
                                    <div class="text-center">
                                        <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
                                    </div>
                                    <form class="user" action="login.html" method="post">
                                        <div class="form-group">
                                            <input type="text" class="form-control form-control-user" name="tendn"
                                                id="exampleInputEmail" aria-describedby="textHelp" required="required"
                                                placeholder="Tên đăng nhập">
                                        </div>
                                        <div class="form-group">
                                            <input type="password" class="form-control form-control-user" name="matkhau"
                                                id="exampleInputPassword" required="required"
                                                placeholder="Mật khẩu">
                                        </div>
                                        <div class="form-group">
                                            <div id="captchaBackground">
                                                 <canvas id="captcha">captcha text</canvas>
                                                 <a id="refreshButton" class="btn btn-user btn-block" href="login.html">Refresh</a>
                                                 <input id="textBox" type="text" name="captcha" required="required"
                                                 	class="form-control form-control-user">                                                 
                                            </div>
                                        </div>
                                        <button class="btn btn-primary btn-user btn-block">
                                            Login
                                        </button>
                                    </form>
                                    <hr>
                                    <div class="text-center">
                                        <a class="small" href="reset_password.html">Quên mật khẩu?</a>
                                    </div>
                                    <div class="text-center">
                                        <a class="small" href="register.html">Chưa có tài khoản? Đăng ký!</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>

    </div>
	
	<script>
		//Tìm và chỉnh sửa lại thẻ <canvas>
		let captchaText = document.querySelector('#captcha');
		var ctx = captchaText.getContext("2d");
		ctx.font = "30px Roboto";
		ctx.fillStyle = "#08e5ff";
		ctx.fillText("${captcha}", captchaText.width/4, captchaText.height/2);
	</script>
<jsp:include page="footer.jsp"></jsp:include>