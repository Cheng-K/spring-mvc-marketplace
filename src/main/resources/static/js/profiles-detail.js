function onProfilePictureChange(event) {
  const defaultProfilePicture = document.querySelector(
    "#default-profile-picture"
  );
  const profilePictureContainer = document.querySelector("#profile-picture");
  defaultProfilePicture.classList.add("d-none");
  profilePictureContainer.classList.remove("d-none");
  const profilePictureImage = profilePictureContainer.querySelector("img");
  profilePictureImage.src = URL.createObjectURL(event.target.files[0]);
  profilePictureImage.onload = () =>
    URL.revokeObjectURL(profilePictureImage.src);
}

const profilePictureUploader = document.querySelector("input[type='file']");
profilePictureUploader.addEventListener("change", onProfilePictureChange);
