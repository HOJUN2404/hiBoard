<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
    
	$("#btnWrite").on("click", function() {
		
		document.bbsForm.hiBbsSeq.value = "";
		document.bbsForm.action = "/board/writeForm";
		document.bbsForm.submit();
		
	});
	
	$("#btnSearch").on("click", function() {
		
		document.bbsForm.hiBbsSeq.value = "";
		document.bbsForm.searchType.value = $("#_searchType").val();
		document.bbsForm.searchValue.value = $("#_searchValue").val();
		document.bbsForm.curPage.value = "1";
		document.bbsForm.action = "/board/list";
		document.bbsForm.submit();
		
	});
});

function fn_view(bbsSeq)
{
	document.bbsForm.hiBbsSeq.value = bbsSeq;
	document.bbsForm.action = "/board/view";
	document.bbsForm.submit();
}

function fn_list(curPage)
{
	document.bbsForm.hiBbsSeq.value = "";
	document.bbsForm.curPage.value = curPage;
	document.bbsForm.action = "/board/list";
	document.bbsForm.submit();	
}
</script>
</head>
<body>
<%@ include file="/WEB-INF/views/include/navigation.jsp" %>
<div class="container">
	
	<div class="d-flex">
      <div style="width:50%;">
         <h2>게시판</h2>
      </div>
      <div class="ml-auto input-group" style="width:50%;">
         <select name="_searchType" id="_searchType" class="custom-select" style="width:auto;">
				<option value="">조회 항목</option>
				<option value="1">작성자</option>
				<option value="2">제목</option>
				<option value="3">내용</option>
			</select>
			<input type="text" name="_searchValue" id="_searchValue" value="" class="form-control mx-1" maxlength="20" style="width:auto;ime-mode:active;" placeholder="조회값을 입력하세요." />
			<button type="button" id="btnSearch" class="btn btn-secondary mb-3 mx-1">조회</button>
      </div>
 	</div>
 	
	<table class="table table-hover">
		<thead>
		<tr style="background-color: #dee2e6;">
			<th scope="col" class="text-center" style="width:10%">번호</th>
			<th scope="col" class="text-center" style="width:55%">제목</th>
			<th scope="col" class="text-center" style="width:10%">작성자</th>
			<th scope="col" class="text-center" style="width:15%">날짜</th>
			<th scope="col" class="text-center" style="width:10%">조회수</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td class="text-center"></td>
			<td>
				<a href="javascript:void(0)" onclick="fn_view()">

				</a>
			</td>
			<td class="text-center"></td>
			<td class="text-center"></td>
			<td class="text-center"></td>
		</tr>
		</tbody>
		<tfoot>
		<tr>
            <td colspan="5"></td>
        </tr>
		</tfoot>
	</table>
	<nav>
		<ul class="pagination justify-content-center">

			<li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list()">이전블럭</a></li>

	
			<li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list()">1</a></li>

			
			<li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="fn_list()">다음블럭</a></li>
		</ul>
	</nav>
	
	<button type="button" id="btnWrite" class="btn btn-secondary mb-3">글쓰기</button>
	
	<form name="bbsForm" id="bbsForm" method="post">
		<input type="hidden" name="hiBbsSeq" value="" />
		<input type="hidden" name="searchType" value="" />
		<input type="hidden" name="searchValue" value="" />
		<input type="hidden" name="curPage" value="" />
	</form>
</div>
</body>
</html>