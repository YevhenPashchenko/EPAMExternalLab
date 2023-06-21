let timer;
let page = 0;
let isLoading = false;
let selectedOption = document.querySelector(".search").querySelector("select").value;
let typedText = document.querySelector("input[type='search']").value;
const waitTime = 1000;
const container = document.querySelector("body");
const resultContainer = document.querySelector("article");
const loadingContainer = document.querySelector(".loading");
const couponTemplate = document.querySelector(".coupon").cloneNode(true);
const topButton = document.querySelector(".top");

container.onscroll = () => {
  let scrollPosition;
  if (typeof document.compatMode !== 'undefined' && document.compatMode !== 'BackCompat') {
    scrollPosition = document.documentElement.scrollTop;
  } else {
    scrollPosition = document.body.scrollTop;
  }
  localStorage.setItem("scroll-position", scrollPosition);
  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
    topButton.style.display = 'block';
  } else {
    topButton.style.display = 'none';
  }
  if (isLoading) return;
  if (Math.ceil(container.clientHeight + container.scrollTop) >= container.scrollHeight) {
    page++;
    fetchData();
  }
}

function fetchData() {
  if (isLoading) return;
  isLoading = true;
  loadingContainer.style.display = "block";
  clearTimeout(timer);
  timer = setTimeout(() => {
    fetch(`http://localhost:8000/scroll?page=${page}&tag=${selectedOption}&text=${typedText}`).then((res) => {
      res.json().then((finalRes) => {
        if (finalRes.length === 0) {
          loadingContainer.style.display = "none";
          return;
        }
        addCouponToContainer(finalRes);
        isLoading = false;
        loadingContainer.style.display = "none";
      });
    }).catch(() => {
      isLoading = false;
      loadingContainer.style.display = "none";
    }).catch(() => {
      isLoading = false;
      loadingContainer.style.display = "none";
    });
  }, 500);
}

document.querySelector(".search").querySelector("select").addEventListener("change", (evt) => {
  selectedOption = evt.currentTarget.value;
  search();
});

document.querySelector("input[type='search']").addEventListener("keyup", (evt) => {
  typedText = evt.currentTarget.value;
  clearTimeout(timer);
  timer = setTimeout(() => {
    search();
  }, waitTime);
});

function search() {
  page = 0;
  isLoading = false;
  resultContainer.innerHTML = "";
  fetch(`http://localhost:8000/search?tag=${selectedOption}&text=${typedText}`).then((res) => {
    res.json().then((finalRes) => {
      addCouponToContainer(finalRes);
    });
  });
}

function addCouponToContainer(finalRes) {
  finalRes.forEach((coupon) => {
    const div = couponTemplate.cloneNode(true);
    div.querySelector("img").setAttribute("src", `./image/${coupon.image}`);
    div.querySelector("img").setAttribute("alt", `${coupon.name}`);
    div.querySelector("h4").textContent = coupon.name;
    div.querySelector("h5").textContent = coupon.short_description;
    div.querySelector("form").previousElementSibling.textContent = `${coupon.price}$`;
    resultContainer.appendChild(div);
  });
}

topButton.addEventListener("click", () => {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
});

window.onload = () => {
  if (localStorage.getItem("scroll-position") != null) {
    document.body.scrollTop = parseInt(localStorage.getItem("scroll-position"));
    document.documentElement.scrollTop = parseInt(localStorage.getItem("scroll-position"));
  }
};