
$(document).ready(function (){
    $("#saveSellerInfoBtn").click(function (){
        fn_saveSellerInfo();
    });

});


// 다음 주소 api
function fn_execDaumPostcode(){
    new daum.Postcode({
        oncomplete: function (data){
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = '';

            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraRoadAddr += data.bname;
            }

            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }

            if(extraRoadAddr !== ''){
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }

            document.getElementById("postcode").value = data.zonecode;
            document.getElementById("roadAddress").value = roadAddr;

            var guideTextBox = document.getElementById("guide");

            if(data.autoRoadAddress) {
                var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                guideTextBox.style.display = 'block';

            } else {
                guideTextBox.innerHTML = '';
                guideTextBox.style.display = 'none';
            }
        }
    }).open();
}

function fn_saveSellerInfo(){
    var companyCeo = $("#companyCeo").val();
    var companyCreateDate = $("#companyCreateDate").val();
    var companyType = $("input[name='companyType']:checked").val();
    var companyAddress = $("#companyAddress").val();
    var companyBusinessType = $("#companyBusinessType").val();
    var companyBusinessCategory = $("#companyBusinessCategory").val();

    $.ajax({
       type: "POST",
       url: "/insertSellerInfo",
       data: JSON.stringify({
          companyCeo: companyCeo,
          companyCreateDate: companyCreateDate,
           companyType: companyType,
           companyAddress: companyAddress,
           companyBusinessType: companyBusinessType,
           companyBusinessCategory: companyBusinessCategory
       }),
        contentType: "application/json",
        success: function (data){
           alert("판매자 정보가 저장되었습니다.");
           window.location.href="/dashboard/sellerInformation";
        },
        error: function (){
           console.log("판매자정보 저장 실패");
        }
    });
}




