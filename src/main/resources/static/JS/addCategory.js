$("#cate1cateId").change(function (){
    var parentId = $(this).val();
    $.ajax({
        url:"/admin/categoryList/",
        type: "GET",
        success:function (response){
            console.log(response);
            var selectedCategory = null;
            for (var i = 0; i < response.length; i++) {
                if (response[i].categoryId == parentId) {
                    selectedCategory = response[i];
                    break;
                }
            }
            if (selectedCategory && selectedCategory.children) {
                var children = selectedCategory.children;

                // cate2cateId의 option 업데이트
                var options = [];
                options.push('<option value="">중분류 선택</option>'); // 기본 옵션
                for (var j = 0; j < children.length; j++) {
                    var child = children[j];
                    options.push(
                        '<option value="' + child.categoryId + '">' + child.categoryName + '</option>'
                    );
                }
                $("#cate2cateId").html(options.join(''));
            }
        },
        error: function (xhr, status, error){
            console.log("중분류 리스트를 가져오는데 실패!");
        }
    });
});

function firstCate(){
    var parentId = $("#cate1ParentID").val();
    var categoryName = $("#cate1Name").val();

    $.ajax({
        url:"/category/add",
        type:"POST",
        data: {
            parentId: parentId,
            name: categoryName,
            depth: 1
        },
        success: function (response){
            alert("대분류가 추가되었습니다.");
        },
        error: function (xhr, status, error){
            console.log("대분류 추가 실패");
        }
    });
}

function secondCate(){
    var parentId = $("#cate2select").val();
    var categoryName = $("#cate2Name").val();

    $.ajax({
        url:"/category/add",
        type:"POST",
        data: {
            parentId: parentId,
            name: categoryName,
            depth: 2
        },
        success: function (response){
            alert("중분류가 추가되었습니다.");
        },
        error: function (xhr, status, error){
            console.log("중분류 추가 실패");
        }
    });
    console.log("parenId ::: ", parentId);
    console.log("categoryName ::: ", categoryName);
}

function thirdCate(){
    var parentId = $("#cate2cateId").val();
    var categoryName = $("#cate3Name").val();

    $.ajax({
        url:"/category/add",
        type:"POST",
        data: {
            parentId: parentId,
            name: categoryName,
            depth: 3
        },
        success: function (response){
            alert("소분류가 추가되었습니다.");
        },
        error: function (xhr, status, error){
            console.log("소분류 추가 실패");
        }
    });
}