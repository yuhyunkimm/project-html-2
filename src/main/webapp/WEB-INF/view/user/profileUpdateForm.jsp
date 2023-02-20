<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ include file="../layout/header.jsp" %>

        <style>
            .container {
                display: flex;
                flex-direction: column;
                align-items: center;
            }

            h2 {
                margin-top: 2rem;
            }

            form {
                width: 50%;
                margin-top: 2rem;
                display: flex;
                flex-direction: column;
                align-items: center;
                border: 1px solid gray;
                padding: 1rem;
                border-radius: 10px;
            }

            .form-group {
                margin-bottom: 1rem;
                text-align: center;
            }

            .form-group img {
                width: 320px;
                height: 270px;
                border-radius: 50%;
                margin-bottom: 1rem;
                border: 1px solid gray;
            }

            .btn {
                margin-top: 1rem;
                width: 20%;
            }
        </style>

        <div class="container my-3">
            <h2 class="text-center">프로필 사진 변경</h2>
            <form id="profileForm" action="/user/profileUpdate" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <img src="${user.profile == null ? '/images/dora.png' : user.profile}" alt="Current Photo"
                        class="img-fluid" id="imagePreview">
                </div>
                <div class="form-group">
                    <input type="file" class="form-control" id="profile" name="profile" onchange="chooseImage(this)">
                </div>
                <button onclick="updateById(${board.id})" class="btn btn-primary">사진변경</button>
            </form>
        </div>

        <script>
            // ajax / put요청을 해야한다면 ajax 사용
            function updateImage() {
                let profileForm = $("#profileForm")[0]; // 배열
                let formData = new FormData(profileForm);

                $.ajax({
                    type: "put",
                    url: "/user/profileUpdate",
                    data: formData,
                    contentType: false, // 필수 : x-www-form-urlencoded로 파싱되는 것을 방지
                    processData: false, // 필수 : contentType을 false로 줬을 때 QueryString으로 자동 설정됨 / false(해제)
                    enctype: "multipart/form-data",
                    dataType: "json" // default : 응답의 mime 타입으로 유추함
                }).done((res) => {
                    alert(res.msg);
                    location.href = "/";
                }).fail((err) => {
                    console.log(err);
                    alert(err.responseJSON.msg);
                });
            }

            function chooseImage(obj) {
                // 메타정보만 읽기 때문에 빠르게 읽는다
                let f = obj.files[0];

                if (!f.type.match("image.*")) { // =mime 타입이 아니라면 
                    alert("이미지를 등록해야 합니다.") // 이미지 파일만 들어오게 설정
                    return;
                }

                // 하드디스크 접근 = IO , 램 접근 = Cash
                // IO는 데이터가 느리다 
                // 이때 받는 것은 IO로 받는다
                //상처를 내서 데이터 기록하는 것을 하드디스크 전류로 데이터저장은 램 컴퓨터가 꺼지면 하드디스크(물리적)만 저장
                // cash는 상대적인 개념이다 (ex 가까운곳 = cashing memory)
                // 자주 쓰는 데이터는 빨리 엑세스 가능하고 cpu가 빠르다고 해도 가깝지 않으면 시간이 걸린다 
                let reader = new FileReader();
                reader.readAsDataURL(f); // 비동기 실행 
                // 파일을 하드디스크(io)에서 가져와서 cpu를 기다리게 만들어 느려진다
                // io시간 동안 멈춰버린다
                // io로 접근 하는 것은 eventq에 등록되어 있고 바로 실행하는 것을 콜스택(ex>style / div ...)이라는 메로리공간에 넣고
                // 현재 scirpt는 콜스택에 들어가지 않는다
                // 사진변경 버튼을 넣을때 콜스택에 들어가면서 readAsDataURL만 io에 event에등록된다
                // 콜스택먼저 실행 io는 오래걸려서.. 콜스택실행후 eventQ로 가서 실행 
                // 멍때리는 것 = 동기적 실행
                // 모든것을 실행후 callback 작업을 해준다

                // 콜스택이 다 비워지고, 이벤트 루프로 가서 readAsDataURL 이벤트가 끝나면 콜백시켜주는 함수
                // onload = callback함수
                // readAsDataURL 안에 onload함수가 있다
                reader.onload = function (e) {//사진 바꿔치기
                    console.log(e);
                    console.log(e.target.result); // =콘텍스트
                    //$("#imagePreview").attr => JQURY
                    $("#imagePreview").attr("src", e.target.result);

                }
            }
        </script>

        <%@ include file="../layout/footer.jsp" %>