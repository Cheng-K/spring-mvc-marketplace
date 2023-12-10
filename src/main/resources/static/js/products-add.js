const imageInput = document.getElementById("image");
const previewImage = document.getElementById("previewImage");

imageInput.addEventListener("change", function () {
  const reader = new FileReader();
  reader.onload = function (e) {
    previewImage.src = e.target.result;
  };
  reader.readAsDataURL(this.files[0]);
});
