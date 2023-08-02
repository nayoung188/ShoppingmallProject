$('#deliveryPolicyAddBtn').click(function (e){
    e.preventDefault();
    $('#staticBackdrop').modal("show");
});

$('#managerAddBtn').click(function (e){
    e.preventDefault();
    $('#managerAddBtnModal').modal("show");
});

$(document).on("click", "#deliveryInfoSubmitBtn", function (){
    fn_addDeliveryInfo();
});

var bankNameMap = {
    "KB": "국민은행",
    "IBK": "기업은행",
    "NH": "농협",
    "SHINHAN": "신한은행",
    "WOORI": "우리은행",
    "SC": "SC제일은행",
    "HANA": 'KEB하나은행',
    "CITY": "한국시티은행",
    "KJB": "광주은행",
    "KNB": "경남은행",
    "DGB": "대구은행",
    "BS": "부산은행",
    "JB": "전북은행",
    "JEJU": "제주은행",
    "SUHYUP": "수협",
    "POST": "우체국",
    "KDB": "산업은행",
    "KFCC": "새마을금고",
    "CU": "신협",
    "FSB": "저축은행",
    "HSBC": "HSBC은행",
    "KBANK": "케이뱅크",
    "KKOBANK": "카카오뱅크",
    "TOSS": "토스뱅크"
}

$(document).ready(function (){

    $('#deliveryInfoDelete').click(function (){
        var deliveryNo = $(this).closest("tr").find(".deliveryNo").val();
        fn_deleteDeliveryInfo(deliveryNo);
    });

    getBusinessLicenseInfo();

    getTelemarketingLicenseInfo();

    $("#saveAccountInfoBtn").click(function() {
        fn_saveAccountInfo();
    });

    var bankName = $("#bankNameInput").val();
    if(bankName != null){
        $("#bankNameInput").val(bankNameMap[bankName]);
    }

    var accountNo = $("#accountNoInput").val();
    var accountDepositor = $("#accountDepositorInput").val();

    var mergedValue = '';
    if (bankName !== '') {mergedValue += bankNameMap[bankName];}
    if (accountNo !== '') {mergedValue += ' ' + accountNo;}
    if (accountDepositor !== '') {mergedValue += ' (예금주 : ' + accountDepositor + ' )';}
    $("#mergedAccountInfo").val(mergedValue);

    $("#bankName li").click(function() {
        $("#bankName li").removeClass("selected");
        $(this).addClass("selected");

        var selectedBank = $(this).text();
        $(".bank_option_button span").text(selectedBank);

        $(".bank_option_list").slideUp();
    });

    $(".bank_option_button").click(function (){
        $(".bank_option_list").slideToggle("slow");
    });

    $("#businessLicenseBtn").click(function (){
       $("#businessLicense").click();
    });

    $("#telemarketingLicenseBtn").click(function (){
        $("#telemarketingLicense").click();
    });

    $("#accountLicenseBtn").click(function (){
        $("#accountLicense").click();
    });

    $("#businessLicense").change(function (){
        uploadBusinessLicense("businessLicense");
    });
    $("#telemarketingLicense").change(function (){
        uploadTelemarketingLicense("telemarketingLicense");
    });
    $("#accountLicense").change(function (){
        uploadAccountLicense("accountLicense");
    });

    showFileName("businessLicenseName");
    showFileName("TelemarketingLicenseInfo");
    showFileName("accountLicenseInfo");

});

$(document).on("click", function (e){
    if(
        !$(e.target).closest(".bank_option_list, .bank_option_button").length
    ) {
        $(".bank_option_list").slideUp();
    }
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

// 배송정보 저장
function fn_addDeliveryInfo(){
    var postcode = $("#postcode").val();
    var roadAddress = $("#roadAddress").val();
    var detailAddress = $("#detailAddress").val();

    var deliveryAddress = postcode + " " + roadAddress + " " + detailAddress;
    var formData = {
        deliveryName: $("#deliveryName").val(),
        deliveryTel: $("#deliveryTel").val(),
        deliveryAddress: deliveryAddress
    };
    $.ajax({
        url: "/insertDelivery",
        method: "POST",
        data: formData,
        success: function (){
            alert("배송정책이 등록되었습니다.");
            $("#deliveryPolicyAddModal").modal("hide");
            location.reload();
        },
        error: function (error){
            console.log("배송정책 저장 실패", error);
        }
    });
}

// 배송정보 삭제
function fn_deleteDeliveryInfo(deliveryNo){
    if(confirm("삭제한 배송 정책은 복구할 수 없습니다. \n 정말 삭제하시겠습니까?")){
        $.ajax({
           url: "/deleteDelivery",
            method: "POST",
            data: {deliveryNo: deliveryNo},
            success: function (){
               alert("삭제가 완료되었습니다.");
               location.reload();
            },
            error: function (){
               console.log("삭제실패");
            }
        });
    }
}

// 사업자등록 조회
function getBusinessLicenseInfo(){
    // serviceKey는 인코딩키 사용해야한다
    let serviceKey = $("#openApiEncodingKey").val();
    var b_no = $("#companyNumberVal").val();
    var data = {"b_no" : [b_no]};

    $.ajax({
       url: "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + serviceKey,
        type: "POST",
        data: JSON.stringify(data),
        dataType: "JSON",
        contentType: "application/json",
        accept: "application/json",
        success: function (result){
           console.log("결과 ::: " , result);
        },
        error: function (error){
           console.log("실패 ::: ", error);
        }
    });
}

// 통신판매업 조회
function getTelemarketingLicenseInfo(){
    // serviceKey는 디코딩키 사용해야한다
    let serviceKey = $("#openApiDecodingKey").val();
    var b_no = $("#companyNumberVal").val();
    var pageNo = 1;
    var numOfRows = 1;
    var resultType = "json";

    $.ajax({
        url: "http://apis.data.go.kr/1130000/MllBs_1Service/getMllBsBiznoInfo_1",
        type: "GET",
        data: {
            "serviceKey": serviceKey,
            "pageNo": pageNo,
            "numOfRows": numOfRows,
            "resultType": resultType,
            "brno": b_no
        },
        dataType: "JSON",
        success: function (result){
           console.log(result);
           displaySellerInformation(result);
        },
        error: function (error){
           console.log(error);
        }
    });
}

// 조회내용 출력
function displaySellerInformation(result){
    if(result.items.length > 0){
        const item = result.items[0];
        $("#prmmiMnno").val(item.prmmiMnno);
    }
}

// 정산정보 저장
function fn_saveAccountInfo(){
    var bankName = $("#bankName li.selected").attr("value");
    var accountDepositor = $("#accountDepositor").val();
    var accountNo = $("#accountNo").val();

    $.ajax({
       type: "POST",
       url: "/insertAccount",
       data: JSON.stringify({
          bankName: bankName,
          accountNo: accountNo,
          accountDepositor: accountDepositor
       }),
        contentType: "application/json",
        success: function (data){
           alert("정산 정보가 저장되었습니다.");
           location.reload();
        },
        error: function (){
           console.log("정산정보 저장 실패");
        }
    });
}



// 사업자 등록증 업로드
function uploadBusinessLicense(fileInputId){
    var fileInput = document.getElementById(fileInputId);
    console.log(fileInput);
    if(!fileInput.files || fileInput.files.length === 0){
        return;
    }
    var formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('licenseType', 'business');
    licenseUploadAJAX(formData);
}

// 통신판매업신고증 업로드
function uploadTelemarketingLicense(fileInputId){
    var fileInput = document.getElementById(fileInputId);
    if(!fileInput.files || fileInput.files.length === 0){
        return;
    }
    var formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('licenseType', 'telemarketing');
    licenseUploadAJAX(formData);
}

// 통장사본 업로드
function uploadAccountLicense(fileInputId){
    var fileInput = document.getElementById(fileInputId);
    if(!fileInput.files || fileInput.files.length === 0){
        return;
    }
    var formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('licenseType', 'account');
    licenseUploadAJAX(formData);
}

// 파일업로드에이잭스
function licenseUploadAJAX(formData){
    $.ajax({
        url: "/uploadLicense",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (){
            alert("파일이 저장되었습니다.");
            location.reload();
        },
        error: function (error){
            console.log("업로드 실패 ::: ", error);
        }
    });
}

function showFileName(inputBoxId){
    var url = $("#" + inputBoxId).val();
    var fileName = url.split('/').pop();
    $("#" + inputBoxId).val(fileName);
}




