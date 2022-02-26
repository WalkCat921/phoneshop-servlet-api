<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
  <header>
    <a href="/">
      <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
      PhoneShop
    </a>
    <a href="${pageContext.servletContext.contextPath}/cart">
      <img class="cart-img" src="${pageContext.servletContext.contextPath}/images/color_cart.png" alt="cart_image"/>
        <p class="mini-cart"><jsp:include page="/cart/miniCart"/></p>
    </a>
  </header>
  <main>
    <jsp:doBody/>
  </main>
  <footer>
    (c) Expert Soft Training
    <br>
    Developed by Egor Zhukovsky
  </footer>
</body>
</html>