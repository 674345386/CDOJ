<%--
Problem statement
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
	<head>

		<title>${targetArticle.title}</title>
	</head>
	<body>
		<div id="article" class="pure-g">
			<div class="pure-u-4-5" id="article_title"
				value="${targetArticle.articleId}">
				<h1>${targetArticle.title}</h1>
			</div>
			<div class="pure-u-1-5">
				<c:if test="${currentUser.type == 1}">
				<div class="pull-right" style="margin: 18px 0;">
					<a href="/admin/article/editor/${targetArticle.articleId}">
						<i class="icon-pencil"></i>
						Edit article
					</a>
				</div>
				</c:if>
			</div>

			<div class="pure-u-1" id="article_content" type="markdown">
				<textarea>${targetArticle.content}</textarea>
			</div>
		</div>

	</body>
</html>
