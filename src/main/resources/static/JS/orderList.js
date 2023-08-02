$(document).ready(function (){
    $('#orderDetailBtn').click(function (){
        var orderIds = $('#orderIds').val();
        $.ajax({
            type: "POST",
            url: "/orderDetail",
            contentType: "application/x-www-form-urlencoded",
            data: "orderIds=" + encodeURIComponent(orderIds),
            success: function (data){
                console.log(data);
                var jsonData = JSON.parse(data);
                console.log(jsonData);
                var tableBody = $('#orderDetailtd');
                var orderData = jsonData[0];
                tableBody.empty();
                for(const key in orderData){
                    if(typeof orderData[key] === 'object'){
                        var innerObject = orderData[key];
                        for (const innerKey in innerObject) {
                            var newRow = $('<tr>');
                            newRow.append('<th>' + innerKey + '</th>');
                            newRow.append('<td>' + innerObject[innerKey] + '</td>');
                            tableBody.append(newRow);
                        }
                    } else {
                        var newRow = $('<tr>');
                        newRow.append('<th>' + key + '</th>');
                        newRow.append('<td>' + orderData[key] + '</td>');
                        tableBody.append(newRow);
                    }
                }
            },
            error: function (xhr, status, error){
                console.log(error);
            }
        });
    });

    $('#naverProductListbtn').click(function (){
       $.ajax({
           type: 'POST',
           url: '/naverProductList',
           success: function (data) {
               var list = JSON.parse(data);
               console.log(list);

               var keysToShow = ["originProductNo", "name", "sellerManagementCode", "statusType", "salePrice", "discountedPrice", "stockQuantity", "regDate"];

               var tableHead = $("#naverProductListHead");
               var tableBody = $("#naverProductListBody");

               var headerRow = $("<tr>");
               headerRow.append($("<th>").text("-"));
               headerRow.append($("<th>").text("상품번호"));
               headerRow.append($("<th>").text("상품명"));
               headerRow.append($("<th>").text("판매코드"));
               headerRow.append($("<th>").text("판매상태"));
               headerRow.append($("<th>").text("정상가"));
               headerRow.append($("<th>").text("할인가"));
               headerRow.append($("<th>").text("재고"));
               headerRow.append($("<th>").text("등록일"));
               tableHead.append(headerRow);

               if (list.contents.length > 0){
                   list.contents.forEach(function (content){
                       content.channelProducts.forEach(function (channelProduct){
                           var row = $("<tr>");
                           for(var key in content){
                               if(key === "channelProducts" || !keysToShow.includes(key)){
                                   continue;
                               }
                               var cell = $("<td>").text(content[key]);
                               row.append(cell);
                           }
                           for(var channelKey in channelProduct){
                               if(keysToShow.includes(channelKey)){
                                   var cell = $("<td>").text(channelProduct[channelKey]);
                                   row.append(cell);
                               }
                           }
                           tableBody.append(row);
                       });
                   });
               }
           },
           error: function (error) {
               console.log(error);
           }
       });
    });
});