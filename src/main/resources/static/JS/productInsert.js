$(document).ajaxStart(function(){
    $('html').css("cursor", "wait");
    $('input').css("pointer-events", "none");
});

$(document).ajaxStop(function(){
    $('html').css("cursor", "auto");
    $('input').css("pointer-events", "auto");
});


/* ai 토글 */
$(document).ready(function (){
    $("#api_toggle_btn").click(function (){
        $(".label_api_container").toggle("slow");
    })
})

$('#frmInsertProd').submit(function(){
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

                   categoryDiv.append(categoryLabel);
                   cateList.push(categoryDiv);
               }
               $("#2depthList").empty().append(cateList);
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
    var input = $('<input>')
        .attr('type', 'hidden')
        .attr('name', 'categoryId')
        .val(categoryId);
    $('#frmInsertProd').append(input);
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



/* vision API, gpt API */
function handleMouseOver(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById("dropZone").classList.add("highlight");
}

function handleMouseOut(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById("dropZone").classList.remove("highlight");
}

function handleDragOver(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById("dropZone").classList.add("highlight");
}

function handleDragLeave(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById("dropZone").classList.remove("highlight");
}

function handleDrop(event) {
    event.preventDefault();
    event.stopPropagation();
    document.getElementById("dropZone").classList.remove("highlight");

    var files = event.dataTransfer.files;
    handleFiles(files);
}

function handleFileInput() {
    var files = document.getElementById("fileInput").files;
    handleFiles(files);
}

function handleFiles(files) {
    if (files && files.length > 0) {
        showImage(files[0]);
        uploadImage(files[0]);
    }
}

function showImage(file) {
    var reader = new FileReader();
    reader.onload = function (event) {
        var imageDiv = document.getElementById("imageDiv");
        var img = document.createElement("img");
        img.src = event.target.result;
        img.classList.add("image_box");
        imageDiv.innerHTML = "";
        imageDiv.appendChild(img);
    };
    reader.readAsDataURL(file);
}

function uploadImage(file) {
    var divGptBtn = document.querySelector(".div_btn_gpt");
    divGptBtn.style.display = "block";

    var formData = new FormData();
    formData.append("files", file);
    $.ajax({
        url: "/upload",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            var labels = response.labels;
            var labelList = document.getElementById("labelList");
            labelList.innerHTML = "";
            labels.forEach(function (label) {
                var labelItem = document.createElement("label");
                var checkbox = document.createElement("input");
                checkbox.type = "checkbox";
                checkbox.name = "labelCheckbox";
                checkbox.value = label;
                checkbox.addEventListener("change", function () {
                    if (this.checked) {
                        var selectedLabel = document.createElement("li");
                        var spanElement = document.createElement("span");
                        spanElement.innerText = "•";
                        selectedLabel.appendChild(spanElement);
                        selectedLabel.innerText += label;
                        var checkedLabelList = document.getElementById("checkedLabelList");
                        checkedLabelList.appendChild(selectedLabel);
                    } else {
                        var checkedLabels = document.querySelectorAll("#checkedLabelList > li");
                        checkedLabels.forEach(function (checkedLabel) {
                            if (checkedLabel.innerText.includes(label)) {
                                checkedLabel.remove();
                            }
                        });
                    }
                });
                labelItem.appendChild(checkbox);
                labelItem.appendChild(document.createTextNode(label));
                labelList.appendChild(labelItem);
            });

            var btnGPT = document.getElementById("btnGPT");
            btnGPT.addEventListener("click", function () {
                var checkedLabels = Array.from(document.querySelectorAll('input[name="labelCheckbox"]:checked')).map(function (checkedCheckbox) {
                    return checkedCheckbox.value;
                });
                if (checkedLabels.length === 0) {
                    alert("추천받고싶은 카테고리를 선택해주세요.");
                } else {
                    sendRecommendation(checkedLabels);
                }
            });

            var imageSources = response.imageSources;
            var imageDiv = document.getElementById("imageDiv");
            imageDiv.innerHTML = "";
            imageSources.forEach(function (imageSource) {
                var img = document.createElement("img");
                img.src = imageSource;
                img.classList.add("image_box");
                imageDiv.appendChild(img);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
        }
    });
}

function sendRecommendation(checkedLabels) {
    console.log("Checked Labels :::", checkedLabels);
    $.ajax({
        url: '/gptCategory',
        type: 'POST',
        data: JSON.stringify({ checkedLabels: checkedLabels }),
        dataType: 'json',
        contentType: 'application/json',
        success: function(recommendations) {
            showRecommendations(recommendations);
        },
        error: function(error) {
            console.error("Error:", error);
        }
    });
}

function showRecommendations(recommendations){
    var divCate = document.getElementById("gptRecommendCategory");
    var divDesc = document.getElementById("gptRecommendDescription");
    divCate.innerHTML = "";
    divDesc.innerHTML = "";

    var isCategorySection = true;

    for (var i = 0; i < recommendations.length; i++) {
        var recommendation = recommendations[i];
        var recommendationContainer = document.createElement("div");
        recommendationContainer.classList.add("recommendation_items");
        var checkboxElement = document.createElement("input");
        checkboxElement.type = "checkbox";
        checkboxElement.value = recommendation;
        checkboxElement.id = recommendation;
        checkboxElement.addEventListener('change', showSelectRecommend);
        var labelElement = document.createElement("label");
        labelElement.innerHTML = recommendation;
        labelElement.setAttribute('for', recommendation);
        recommendationContainer.appendChild(checkboxElement);
        recommendationContainer.appendChild(labelElement);

        if (recommendation === "카테고리") {
            isCategorySection = true;
        } else if (recommendation === "상세설명") {
            isCategorySection = false;
        } else {
            if (isCategorySection) {
                divCate.appendChild(recommendationContainer);
            } else {
                divDesc.appendChild(recommendationContainer);
            }
        }

    }
    var divGptResult = document.querySelector(".div_gpt_result");
    divGptResult.style.display = "block";
}



function showSelectRecommend(){
    var selectRecommendCategory = document.getElementById("selectRecommendCategory");
    var selectRecommendDescription = document.getElementById("selectRecommendDescription");

    selectRecommendCategory.innerHTML = "";
    selectRecommendDescription.innerHTML = "";

    var categoryCheckboxes = document.querySelectorAll("#gptRecommendCategory input[type='checkbox']");
    var descriptionCheckboxes = document.querySelectorAll("#gptRecommendDescription input[type='checkbox']");

    for (var i = 0; i < categoryCheckboxes.length; i++) {
        if (categoryCheckboxes[i].checked) {
            var selectedRecommendation = document.createElement("div");
            selectedRecommendation.innerHTML = categoryCheckboxes[i].value;
            selectRecommendCategory.appendChild(selectedRecommendation);
        }
    }
    if (selectRecommendCategory.childElementCount > 0) {
        var gptRecommTitle = document.createElement("div");
        gptRecommTitle.classList.add("gptRecommTitle");
        gptRecommTitle.innerHTML = "▶ 선택 키워드";
        selectRecommendCategory.insertBefore(gptRecommTitle, selectRecommendCategory.firstChild);
    }

    for (var j = 0; j < descriptionCheckboxes.length; j++) {
        if (descriptionCheckboxes[j].checked) {
            var selectedRecommendation = document.createElement("div");
            selectedRecommendation.innerHTML = descriptionCheckboxes[j].value;
            selectRecommendDescription.appendChild(selectedRecommendation);
        }
    }
    if (selectRecommendDescription.childElementCount > 0) {
        var gptRecommTitle = document.createElement("div");
        gptRecommTitle.classList.add("gptRecommTitle");
        gptRecommTitle.innerHTML = "▶ 선택 추천 상세설명 문구";
        selectRecommendDescription.insertBefore(gptRecommTitle, selectRecommendDescription.firstChild);
    }
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
    for(var image of event.target.files){
        var reader = new FileReader();
        reader.onload = function (event){
            var img = document.createElement("img");
            img.setAttribute("src", event.target.result);
            img.setAttribute("class", "sub_thumbnail_img");
            var div= document.querySelector("div#subThumbnails");
            var i = document.querySelector("#subThumbnail i.fa-regular.fa-image");
            if(i){
                document.getElementById("subThumbnail").removeChild(i);
            }
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

function addOptionInput(){
    var container = document.createElement("div");
    container.className = "option_input_container";

    var inputBoxDiv = document.createElement("div");
    inputBoxDiv.className = "input_box_div";

    var input = document.createElement("input");
    input.type = "text";
    input.className = "input_text_style";
    inputBoxDiv.appendChild(input);

    var minusIconDiv = document.createElement("div");
    minusIconDiv.className = "minus_icon_div";
    var minusIcon = document.createElement("i");
    minusIcon.className = "fa-regular fa-square-minus";
    minusIcon.onclick = deleteOptionInput;
    minusIconDiv.appendChild(minusIcon);

    container.appendChild(inputBoxDiv);
    container.appendChild(minusIconDiv);

    var divInfo = document.querySelector(".option_box");
    divInfo.appendChild(container);
}

function deleteOptionInput(){
    var container = this.parentNode.parentNode;
    container.remove();
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