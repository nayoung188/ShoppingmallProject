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

// let slides = document.querySelector(".image_slide"),
//     slide = document.querySelectorAll(".image_slide li"),
//     currentIdx = 0,
//     slideCount = slide.length,
//     slideWidth = 300,
//     slideMargin = 30,
//     prevBtn = document.querySelector(".prev"),
//     nextBtn = document.querySelector(".next");
//
// makeClone();
//
// function makeClone(){
//     for(let i=0; i <slideCount; i++){
//         let cloneSlide = slide[i].cloneNode(true);
//         cloneSlide.classList.add("clone");
//         slides.appendChild(cloneSlide);
//     }
//
//     for(let i=slideCount -1; i >=0; i--){
//         let cloneSlide = slide[i].cloneNode(true);
//         cloneSlide.classList.add("clone");
//         slides.prepend(cloneSlide);
//     }
//
//     updateWidth();
//     setinit();
//     setTimeout(function (){
//         slides.classList.add("animated");
//     }, 100);
// }
//
// function updateWidth(){
//     let currentSlides = document.querySelectorAll(".image_slide li");
//     let newSlideCount = currentSlides.length;
//     let newWidth = (slideWidth + slideMargin) * newSlideCount - slideMargin + "px";
//     slides.style.width = newWidth;
// }
//
// function setinit(){
//     let translateValue = -(slideWidth + slideMargin) * slideCount;
//     slides.style.transform = "translateX(" + translateValue + "px)";
// }

$(document).ready(function (){
    $('.slide_div').slick({

    });
});
