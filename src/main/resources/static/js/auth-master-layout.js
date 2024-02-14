const myCarousel = document.getElementById("carousel");
const carouselPageCircles = document.querySelectorAll(".carousel-page-circle");

myCarousel.addEventListener("slide.bs.carousel", (event) => {
  const fromPage = event.from;
  const toPage = event.to;
  carouselPageCircles[fromPage].classList.remove("bg-primary");
  carouselPageCircles[toPage].classList.add("bg-primary");
});
