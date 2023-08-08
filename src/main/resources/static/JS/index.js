function displayCounselModal(){
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function (){
        if(xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("counselFormContainer").innerHTML = xhr.responseText;
            document.getElementById("counselModal").style.display = "block";
            document.body.style.overflow = "hidden";
        }
    };
    xhr.open("GET", "/counselForm", true);
    xhr.send();
}
function hideCounselModal() {
    document.getElementById("counselModal").style.display = "none";
    document.body.style.overflow = "";
}

$(document).ready(function (){
    // slick 라이브러리를 사용한 무한츠쿠요미
    $('.slide_div').slick({
        slide: 'div',
        infinite: true,
        arrows: true
    });
});
