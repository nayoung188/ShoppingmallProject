var productStateValue = $("#productStateValue").val();

$("input[name='productState']").filter(function (){
    return $(this).val() === productStateValue;
}).prop("checked", true);


var parentCateId = $('#parentCateId').val();
var grandParentCateId = $('#grandParentCateId').val();
var cateId = $('#categoryValue').val();

$("input[name='selectedCategory']").filter(function (){
    return $(this).data('category-id') == grandParentCateId;
}).prop('checked', true).each(function (){
    var event = new Event('change');
    this.dispatchEvent(event);
});

$("input[name='selected2Category']").filter(function (){
    return $(this).data('category-id') == parentCateId;
}).prop('checked', true).each(function (){
    var event = new Event('change');
    this.dispatchEvent(event);
});

$(document).ready(function (){
    var mainImagesPath = $('#mainImagePath').val();
    var mainImgElement = $('<img>').attr('src', mainImagesPath).addClass('thumbnail_img');
    $('#mainThumbnail').empty().append(mainImgElement);
    $('#mainThumbnail i').remove();

    $('.sub_image_span').each(function (){
        var subImagesPath = $(this).find('input[type="hidden"]').val();
        if(subImagesPath){
            var subImagElement = $('<img>').attr('src', subImagesPath).addClass('sub_thumbnail_img');
            $('#subThumbnails').append(subImagElement);
            $('#subThumbnail i').remove();
        }
    });

    var subImageNames = [];

    $('[id^="subImageName"]').each(function() {
        var name = $(this).val();
        subImageNames.push(name);
    });

    var combinedNames = subImageNames.join(', ');
    $('.sub_file_name').val(combinedNames);


    $('input[name="originalPrice"]').each(function() {
        var originalPriceValue = $(this).val();
        $(this).val(comma(uncomma(originalPriceValue)));
    });

    $('input[name="salePrice"]').each(function() {
        var salePriceValue = $(this).val();
        $(this).val(comma(uncomma(salePriceValue)));
    });
});

$('#frmModifyProd').submit(function(){
    $('input[name="originalPrice"]').each(function() {
        var originalPriceValue = $(this).val();
        $(this).val(uncomma(originalPriceValue));
    });
    $('input[name="salePrice"]').each(function() {
        var salePriceValue = $(this).val();
        $(this).val(uncomma(salePriceValue));
    });
})


/* 카테고리 */
function showDepth2List(element){
    var selectedRadio = event.target;
    var categoryName = selectedRadio.nextElementSibling.textContent.trim(); // 카테고리 이름 가져오기
    var selectCateNo = 1;
    showSelectCateName(categoryName, selectCateNo);
    var parentId = element.getAttribute('data-category-id');
    var prevParentId = document.querySelector('.first_cate_name.selected')?.getAttribute('data-category-id');
    if (parentId !== prevParentId) {
        $('#3depthList').empty();
    }
    changeBackground('selectedCategory');
    $.ajax({
        url:"/admin/categoryList",
        type:"GET",
        success: function (response){
            var selectCate = null;
            for( var i = 0; i < response.length; i++){
                if(response[i].categoryId == parentId) {
                    selectCate = response[i];
                    break;
                }
            }
            if(selectCate && selectCate.children){
                var children = selectCate.children;
                var cateList = [];
                for(var j = 0; j < children.length; j++){
                    var child = children[j];
                    var categoryDiv = $('<div>').addClass('depth_list').addClass('category_hover');
                    var radioId = 'selected2Category_' + child.categoryId;
                    var radioInput = $('<input>')
                        .attr('type', 'radio')
                        .attr('name', 'selected2Category')
                        .attr('id', radioId)
                        .addClass('depth2_radio')
                        .attr('data-category-id', child.categoryId)
                        .val(child.categoryName)
                        .on('change', function (){
                            var parentId = $(this).data('category-id');
                            showDepth3List(parentId);
                            var cateName = $(this).val();
                            var selectCateNo = 2;
                            showSelectCateName(cateName, selectCateNo);
                            changeBackground('selected2Category');
                        });

                    var categorySpan = $('<span>')
                        .text(child.categoryName)
                        .addClass('depth2_cate');

                    var categoryLabel = $('<label>')
                        .attr('for', radioId)
                        .addClass('radio_span_label')
                        .append(radioInput)
                        .append(categorySpan)
                        .append('<span><i class="fa-solid fa-caret-right fa-xs" style="color: #858796;"></i></span>');
                    if(child.categoryId == parentCateId){
                        radioInput.prop('checked', true).trigger('change');

                    }
                    categoryDiv.append(categoryLabel);
                    cateList.push(categoryDiv);
                }
                $("#2depthList").empty().append(cateList);
                changeBackground('selected2Category');
            }
        } ,
        error: function (xhr, status, error){
            console.log("중분류 출력 실패!@@!!");
        }
    });
}

function showDepth3List(parentId){
    $.ajax({
        url:"/admin/categoryList",
        type:"GET",
        success:function (response){
            var cateList = [];
            for(var i=0; i < response.length; i++){
                var category = response[i];
                if(category.children && category.children.length > 0){
                    var children = category.children;
                    for( var j=0; j < children.length; j++) {
                        var child = children[j];
                        if(child.categoryId == parentId){
                            var subchildren = child.children;
                            if(subchildren && subchildren.length > 0){
                                for(var k=0; k < subchildren.length; k++){
                                    var subchild = subchildren[k];
                                    if(subchild.parentId == parentId){
                                        var categoryDiv = $('<div>').addClass('depth_list').addClass('category_hover');
                                        var radioId = 'selected3Category_' + subchild.categoryId;
                                        var radioInput = $('<input>')
                                            .attr('type','radio')
                                            .attr('id', radioId)
                                            .attr('name', 'selected3Category')
                                            .addClass('depth3_radio')
                                            .attr('data-category-id', subchild.categoryId)
                                            .val(subchild.categoryName)
                                            .on('change', function (){
                                                var categoryId = $(this).data('category-id');
                                                saveCategoryId(categoryId);
                                                var cateName = $(this).val();
                                                var selectCateNo = 3;
                                                showSelectCateName(cateName, selectCateNo);
                                                changeBackground('selected3Category');
                                            });
                                        var categorySpan = $('<span>')
                                            .text(subchild.categoryName)
                                            .addClass('depth3_cate');

                                        var categoryLabel = $('<label>')
                                            .attr('for', radioId)
                                            .addClass('radio_span_label')
                                            .append(radioInput)
                                            .append(categorySpan)

                                        if(subchild.categoryId == cateId){
                                            radioInput.prop('checked', true).trigger('change');
                                        }
                                        categoryDiv.append(categoryLabel);
                                        cateList.push(categoryDiv);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            $('#3depthList').empty().append(cateList);
            changeBackground('selected3Category');
        },
        error: function (xhr, status, error){
            console.log("소분류 출력 실패~!@###@@");
        }
    });
}

function changeBackground(radioName){
    var checkedRadio = $('input[name="' + radioName + '"]:checked');
    checkedRadio.closest('div').css('background-color','rgba(78, 115, 223, 0.07)');

    var uncheckedRadio = $('input[name="' + radioName + '"]:not(:checked)');
    uncheckedRadio.closest('div').css('background-color', '');
}


function saveCategoryId(categoryId){
    $('#categoryValue').val(categoryId);
}

function showSelectCateName(categoryName, selectCateNo){
    // var selectCateName = document.querySelector('.select_cate_name_' + selectCateNo);
    // selectCateName.textContent = categoryName;
    var selectCateBoxes = document.querySelectorAll('.select_cate_name_box');
    if (selectCateNo === 1) {
        for (var i = 1; i < selectCateBoxes.length; i++) {
            selectCateBoxes[i].textContent = "";
        }
    } else if (selectCateNo === 2) {
        selectCateBoxes[2].textContent = "";
    }
    selectCateBoxes[selectCateNo - 1].textContent = categoryName;
}


/* 업로드파일 썸네일 */

function setMainThumbnail(event){
    var reader = new FileReader();
    reader.onload = function (event){
        var img = document.createElement("img");
        img.setAttribute("src", event.target.result);
        img.setAttribute("class", "thumbnail_img");
        var div = document.querySelector("div#mainThumbnail");
        var existingImg = div.querySelector("img.thumbnail_img");
        if (existingImg) {
            div.removeChild(existingImg);
        }
        var i = div.querySelector("i.fa-regular.fa-image");
        if(i){
            div.removeChild(i);
        }
        div.appendChild(img);
    };
    reader.readAsDataURL(event.target.files[0]);
    showMainFileName(event);
}

function setSubThumbnail(event){
    var div= document.querySelector("div#subThumbnails");
    div.innerHTML = "";
    var i = document.querySelector("#subThumbnail i.fa-regular.fa-image");
    if (i) {
        i.remove(); // 아이콘을 제거합니다
    }
    for(var image of event.target.files){
        var reader = new FileReader();
        reader.onload = function (event){
            var img = document.createElement("img");
            img.setAttribute("src", event.target.result);
            img.setAttribute("class", "sub_thumbnail_img");
            div.appendChild(img);
        }
        reader.readAsDataURL(image);
    }
    showSubFileName(event);
}

function mainFileUpload(){
    document.getElementById('mainImage').click();
}

function subFileUpload(){
    document.getElementById('subImages').click();
}

function showMainFileName(event){
    var fileinput = event.target;
    var filename = fileinput.files[0].name;
    $('.main_file_name').val(filename);
}

function showSubFileName(event){
    var fileinput = event.target;
    var filename = "";
    for (var i = 0; i < fileinput.files.length; i++) {
        filename += fileinput.files[i].name + ", ";
    }
    filename = filename.slice(0, -2);
    $('.sub_file_name').val(filename);
}

function inputPriceFormat(price){
    price.value = comma(uncomma(price.value));
}

function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

function uncomma(str) {
    str = String(str);
    return str.replace(/[^\d]+/g, '');
}

function modifyCheck(){
    if(confirm("상품의 정보를 수정하시겠습니까?")){
        return true;
    } else {
        return false;
    }
}