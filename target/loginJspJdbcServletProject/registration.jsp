<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Sign Up Form by Colorlib</title>

<!-- Font Icon -->
<link rel="stylesheet"
	href="fonts/material-icon/css/material-design-iconic-font.min.css">

<!-- Main css -->
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<input type="hidden" id= "status" value="<%= request.getAttribute("status") %>">

	<div class="main">

		<!-- Sign up form -->
		<section class="signup">
			<div class="container">
				<div class="signup-content">
					<div class="signup-form">
						<h2 class="form-title">Sign up</h2>
					
						<form method="post" action="registerurl" class="register-form"
							id="register-form">
							<div class="form-group">
								<label for="name"><i
									class="zmdi zmdi-account material-icons-name"></i></label> <input
									type="text" name="name" id="name" placeholder="Your Name" autofocus autocomplete="on" required />
							</div>
							<div class="form-group">
								<label for="email"><i class="zmdi zmdi-email"></i></label> <input
									type="email" name="email" id="email" placeholder="Your Email" autocomplete="on" required/>
							</div>
							<div class="form-group">
								<label for="pass"><i class="zmdi zmdi-lock"></i></label> <input
									type="password" name="pass" id="pass" placeholder="Password" required/>
							</div>
							<div class="form-group">
								<label for="re_pass"><i class="zmdi zmdi-lock-outline"></i></label>
								<input type="password" name="re_pass" id="re_pass"
									placeholder="Repeat your password" onkeyup="check();" required/>
								<span id="message"></span>
							</div>
							<div class="form-group">
								<label for="contact"><i class="zmdi zmdi-lock-outline"></i></label>
								<input type="text" name="contact" id="contact"
									placeholder="Contact no" autocomplete="on" required/>
							</div>
							<div class="form-group">
								<input type="checkbox" name="agree-term" id="agree-term"
									class="agree-term" /> <label for="agree-term"
									class="label-agree-term"><span><span></span></span>I
									agree all statements in <a href="#" class="term-service">Terms
										of service</a></label>
							</div>
							<div class="form-group form-button">
								<input type="submit" name="signup" id="signup"
									class="form-submit" value="Register" />
							</div>
						</form>
					</div>
					<div class="signup-image">
						<figure>
							<img src="images/signup-image.jpg" alt="sing up image">
						</figure>
						<a href="login.jsp" class="signup-image-link">I am already
							member</a>
					</div>
				</div>
			</div>
		</section>


	</div>
	<!-- JS -->
<%--	<script src="vendor/jquery/jquery.min.js"></script>--%>
	<script src="js/main.js"></script>
<%--	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>--%>
<%--	<link rel="stylesheet" href="alert/dist/sweetalert.css">--%>
<script src="https://common.olemiss.edu/_js/sweet-alert/sweet-alert.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://common.olemiss.edu/_js/sweet-alert/sweet-alert.css">
<script type="text/javascript">
	const status = document.getElementById("status").value;
	if(status==="succeeded"){
// swal("Good job", "Your Sign up was a success");
		swal({
			title: "Success",
			text: "Your Registration was a success",
			type: "success",
			confirmButtonText: "Ok"
		});
	}else if(status==="failed"){
		// swal("Not Good", "Sign Up Failed");
		swal({
			title: "Error!",
			text: "Registration failed",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="psql"){
		swal({
			title: "Error!",
			text: "Your Phone or email is already registered with us",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="NoMatch"){
		swal({
			title: "Error!",
			text: "Password do not match!",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="emptyname"){
		swal({
			title: "Error!",
			text: "Please enter thy name",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="emptyemail"){
		swal({
			title: "Error!",
			text: "Please enter thy email",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="emptyphone"){
		swal({
			title: "Error!",
			text: "Please enter thy phone",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="emptypass"){
		swal({
			title: "Error!",
			text: "Please enter thy password",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="emptyrepeatPwd"){
		swal({
			title: "Error!",
			text: "Ensure thy password matches thy previous one",
			type: "error",
			confirmButtonText: "Ok"
		});
	}else if(status==="usernotavailable"){
		swal({
			title: "Have you used this app before?",
			text: "Please key in your details and register with us",
			type: "error",
			confirmButtonText: "Ok"
		});
	}
</script>



</body>
<!-- This templates was made by Colorlib (https://colorlib.com) -->
</html>