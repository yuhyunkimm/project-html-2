<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file ="../layout/header.jsp"%>


    <div class="container my-3">
            <form>
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Enter title" name="title" id="title" value="${dto.title}">
                </div>

                <div class="form-group">
                    <textarea class="form-control summernote" rows="5" id="content" name="content">
                        ${dto.content}
                    </textarea>
                </div>
            </form>
            <button onClick="updateById(${dto.id})" class="btn btn-primary">글수정완료</button>
    </div>

    <script>
        $('.summernote').summernote({
            tabsize: 2,
            height: 400
        });

        function updateById(id) {
            $.ajax({
                type: "put",
                url: `/board+${post.id}+/boardUpdateForm`, //쿼리스트링수정
                data: JSON.stringify(post),
                headers: {
                    "Content-type": "application/json; charset=UTF-8",
                },
                dataType: "json"
            }).done((res) => {
                alert(res.msg);
                location.href = "/board";
            }).fail((err) => {
                alert(res.msg);
            });
        }

    </script>
    <%@ include file ="../layout/footer.jsp"%>
