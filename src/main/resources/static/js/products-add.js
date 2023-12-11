const imageInput = document.getElementById("imageFile");
const previewImage = document.getElementById("previewImage");

imageInput.addEventListener("change", function () {
  const reader = new FileReader();
  reader.onload = function (e) {
    previewImage.src = e.target.result;
  };
  reader.readAsDataURL(this.files[0]);
});
