// div클릭하면 조회
function searchSubmit(divId, value){
    var formElement = document.getElementById('productSearchForm');
    var productStateElement = formElement.querySelectorAll('input[name="productState"]');
    productStateElement.forEach(function(element) {
        element.checked = element.value === value;
    });
    formElement.submit();
}

var allCountDiv = document.getElementById('allCountDiv');
allCountDiv.addEventListener('click', function (){
    searchSubmit('allCountDiv','');
});

var saleCountDiv = document.getElementById('saleCountDiv');
saleCountDiv.addEventListener('click', function (){
   searchSubmit('saleCountDiv','판매중');
});

var soldOutCountDiv = document.getElementById('soldOutCountDiv');
soldOutCountDiv.addEventListener('click', function (){
    searchSubmit('soldOutCountDiv','품절');
});

var stopCountDiv = document.getElementById('stopCountDiv');
stopCountDiv.addEventListener('click', function (){
    searchSubmit('stopCountDiv','판매중지');
});


// 검색어 유지
window.addEventListener('DOMContentLoaded', function() {
    var searchFields = document.querySelectorAll('.search_input');
    searchFields.forEach(function(field) {
        field.value = new URLSearchParams(window.location.search).get(field.name) || '';
    });
    var checkboxes = document.querySelectorAll('input[type="checkbox"][name="productState"]');
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = new URLSearchParams(window.location.search).getAll(checkbox.name).includes(checkbox.value);
        checkbox.addEventListener('change', function() {
            updateSearchParameters();
        });
    });

    var searchForm = document.getElementById('productSearchForm');
    searchForm.addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 제출 동작 중단
        updateSearchParameters();
        this.submit(); // 업데이트된 URL로 폼 제출
    });

    function updateSearchParameters() {
        var searchParams = new URLSearchParams(window.location.search);
        checkboxes.forEach(function(checkbox) {
            if (checkbox.checked) {
                searchParams.append(checkbox.name, checkbox.value);
            } else {
                searchParams.delete(checkbox.name);
            }
        });
        var pageSize = $('input[name="pageSize"]').val();
        searchParams.set('pageSize', pageSize);
        var newUrl= window.location.pathname + '?' + searchParams.toString();
        window.history.replaceState({}, '', newUrl);
    }
});

function setSearchParam(page){
    var searchParam = new URLSearchParams(window.location.search);
    searchParam.set('page', page);
    window.location.href = '?' + searchParam.toString();
}


// 초기화버튼
$("#inputReset").click(function (){
   $("input[class='search_input']").val('');
   $("input[type=checkbox]").prop('checked', false);
});

function generateSearchParameters() {
    var searchKeyword = document.getElementById('searchKeyword').value();
    var productName = document.getElementById('productName').value();
    var productCode = document.getElementById('productCode').value();
    var productBrand = document.getElementById('productBrand').value();
    var manufacturer = document.getElementById('manufacturer').value();
    var productState = document.getElementById('productState').value();

    var parameters = '';

    if (searchKeyword != null) {
        parameters += '&searchKeyword=' + encodeURIComponent(searchKeyword);
    }

    if (productName != null) {
        parameters += '&productName=' + encodeURIComponent(productName);
    }

    if (productCode != null) {
        parameters += '&productCode=' + encodeURIComponent(productCode);
    }

    if (productBrand != null) {
        parameters += '&productBrand=' + encodeURIComponent(productBrand);
    }

    if (manufacturer != null) {
        parameters += '&manufacturer=' + encodeURIComponent(manufacturer);
    }

    if (productState != null) {
        for (var i = 0; i < productState.length; i++) {
            parameters += '&productState=' + encodeURIComponent(productState[i]);
        }
    }
    return parameters;
}

function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

$(document).ready(function() {
    $('td[name="originalPrice"]').each(function() {
        var originalPriceValue = $(this).text();
        $(this).text(comma(originalPriceValue));
    });

    $('td[name="salePrice"]').each(function() {
        var salePriceValue = $(this).text();
        $(this).text(comma(salePriceValue));
    });

    $('#stateToggle').click(function (){
       $('#stateToggleDiv').toggle();
    });

    $('#pageSizeToggle').click(function (){
       $('#pageSizeToggleDiv').toggle();
    });

    $('.pageSizeOption').click(function() {
        var pageSize = parseInt($(this).data('value'));
        $('input[name="pageSize"]').val(pageSize);

        if(pageSize == null){
            pageSize = 10;
        }

        var searchParams = new URLSearchParams(window.location.search);
        searchParams.set('pageSize', pageSize);

        var newURL = window.location.pathname + '?' + searchParams.toString();
        window.location.href = newURL;
    });
});
// var productTable = $('#productTable');
// function loadProductList(pageSize){
//     var searchParams = new URLSearchParams(window.location.search);
//     searchParams.delete('page');
//     searchParams.set('pageSize', pageSize);
//     console.log("펑션 내부 pageSize ::: ", pageSize);
//     var url = window.location.pathname + '?' + searchParams.toString();
//
//     if(pageSize === undefined){
//         pageSize = 10;
//     }
//
//     $.ajax({
//         url: url,
//         type: 'GET',
//         success: function(data) {
//             productTable.html($(data).find('#productTable').html());
//         },
//         error: function() {
//             console.log('Error occurred while loading product list.');
//         }
//     });
// }



$('#checkAll').click(function (){
   if(this.checked){
       $("input[name='listCheckBox']").prop("checked", true);
   } else {
       $("input[name='listCheckBox']").prop("checked", false);
   }
});

$("input[name='listCheckBox']").click(function (){
   let length = $("input[name='listCheckBox']").length;
   let checkLength = $("input[name='listCheckBox']:checked").length;
   if(length == checkLength){
       $("#checkAll").prop("checked", true);
   } else {
       $("#checkAll").prop("checked", false);
   }
});

function fnDelete(){
    let checkLength = $("input[name='listCheckBox']:checked").length;
    if(checkLength > 0){
        if(confirm("선택한 상품을 삭제하시겠습니까?")){
            if(confirm("삭제한 상품은 되돌릴 수 없습니다.\n정말 삭제하시겠습니까?")){
                let form = $("#listForm");
                form.attr("action", "/deleteProduct");
                form.attr("method", "POST");

                let productNos = [];
                $("input[name='listCheckBox']:checked").each(function (){
                    productNos.push($(this).val());
                });
                productNos.forEach(function (productNo){
                    $("<input>").attr({
                        type: "hidden",
                        name: "productNo",
                        value: productNo
                    }).appendTo(form);
                });
                form.submit();
                alert("삭제가 완료되었습니다.");
            }
        } else {
            return false;
        }
    }else {
        alert("삭제할 상품을 선택해주세요!");
    }
}

function fnStateUpdate(productState){
    let checkedProductNos = [];
    $("input[name='listCheckBox']:checked").each(function() {
        checkedProductNos.push($(this).val());
    });
    if(checkedProductNos.length > 0){
        if(confirm("선택한 상품의 상태를 변경하시겠습니까?")){
            let frm = $("#listForm");
            frm.attr("action", "/updateProductState");
            frm.attr("method", "POST");

            frm.append("<input type='hidden' name='productState' value='" + productState + "'>");
            for (let i = 0; i < checkedProductNos.length; i++) {
                frm.append("<input type='hidden' name='productNo' value='" + checkedProductNos[i] + "'>");
            }
            frm.submit();
            alert("상태 변경이 완료되었습니다.");
        } else {
            return false;
        }
    } else {
        alert("상태를 변경할 상품을 선택해주세요!");
    }
}

function fnExcelDown(){
    let checkedProductNos = [];
    $("input[name='listCheckBox']:checked").each(function() {
        checkedProductNos.push($(this).val());
    });
    if(checkedProductNos.length > 0){
        let frm = $("#listForm");
        frm.attr("action", "/downloadExcel");
        frm.attr("method", "POST");
        let productNos = [];
        $("input[name='listCheckBox']:checked").each(function (){
            productNos.push($(this).val());
        });
        productNos.forEach(function (productNo){
            $("<input>").attr({
                type: "hidden",
                name: "productNo",
                value: productNo
            }).appendTo(frm);
        });
        frm.submit();
    }
}

