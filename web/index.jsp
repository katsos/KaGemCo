<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    
    <!-- Meta -->
    <!meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Nikos Katsos">

    <title>KaGemCo Communications</title>

    <!-- CSS -->
    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    <link href="css/responsive.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<header>
    <!-- Navigation -->
    <nav class="navbar-default navbar-fixed-top" role="navigation">
        <div class="container">

            <!-- Responsive NavBar -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html"> KaGemCo </a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="index.html"> Αρχική </a></li>
                    <li><a href="offers.html"> Προσφορές </a></li>
                    <li><a href="stores.html"> Καταστήματα </a></li>
                    <li><a href="contact.html"> Επικοινωνία </a></li>
                </ul>
            </div>
        </div>
    </nav>
</header>
    
<main>
	<div class="container">
        <div id="top-jumb" class="Jumbotron row">
            <div id="moto" class="float-left col-lg-9 col-md-7 col-sm-8">
					<h1> Κάνουμε την επικοινωνία και την οικονομία συνώνυμα - KG</h1>
					<p> Telephony is commonly referred to as the construction or operation of telephones and telephonic systems and as a system of telecommunications in which telephonic equipment is employed in the transmission of speech or other sound between points, with or without the use of wires. 
					<br>The term is also used frequently to refer to computer hardware, software, and computer network systems, that perform functions traditionally performed by telephone equipment. In this context the technology is specifically referred to as Internet telephony, or voice over Internet Protocol (VoIP). 
					</p>	
				</div> <!-- /moto -->
				
            <div id="login-form" class="float-right col-lg-3 col-md-5 col-sm-4">
					<form class="form col-md-12 center-block">
						<div class="form-group">
							<input type="text" class="form-control input-lg" placeholder="Username">
						</div>
						<div class="form-group">
							<input type="password" class="form-control input-lg" placeholder="Password">
						</div>
						<div class="form-group">
							<button class="btn btn-primary btn-lg btn-block">Sign In</button>
							<span class="pull-right"><a href="#">Register</a></span><span><a href="#">Need help?</a></span>
						</div>
					</form>
				</div> <!-- /login -->
        </div> <!-- /top-jumb -->
            
        <div id="prosfores" class="row Jumbotron">
						<div id="prosf1" class=" col-lg-4">
								<!--<img src="..." alt="...">-->
								<div class="caption">
									<h3>The title is real1</h3>
									<p>The paragraph is real1 and mpla mpla and other mpla mpla and more more more more mpla and mplou and mple and anius day mpla mplou mpla.</p>
									<p><a class="prosf-more">More...</a></p>
								</div>
						</div>

						<div id="prosf2" class="col-lg-4">
								<!--<img src="..." alt="...">-->
								<div class="caption">
									<h3>The title is real2</h3>
									<p>The paragraph is real2 and mpla mpla and other mpla mpla and more more more more mpla and mplou and mple and anius day mpla mplou mpla.</p>
									<p><a class="prosf-more">More...</a></p>
								</div>
						</div>

						<div id="prosf3" class="col-lg-4">
								<!--<img src="..." alt="...">-->
								<div class="caption">
									<h3>The title is real3</h3>
									<p>The paragraph is real3 and mpla mpla and other mpla mpla and more more more more mpla and mplou and mple and anius day mpla mplou mpla.</p>
									<p><a class="prosf-more">More...</a></p>
								</div>
						</div>
				</div> <!-- /prosfores -->
    </div> <!-- /container -->
</main>

<footer>
    <div class="container">
        <span id="copyright">Copyright &copy; KatsoGemoCompany 2015.</span>
        <span id="made" class="hidden-xs"> Made with 
            <a href="https://spring.io/tools"> Spring Tool Suite</a>, 
            <a href="http://getbootstrap.com/"> BootStrap</a>, 
            <a href="http://brackets.io/"> Brackets</a>.
        </span>
    </div>
</footer>

<!-- Scripts -->
    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>
    
    <!-- BootStrapsJS CDN --!>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" 
          integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" 
          crossorigin="anonymous"></script>
<!-- /Scripts -->
</body>

</html>
