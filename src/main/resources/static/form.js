// form.js
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("bookForm");

    form.addEventListener("submit", function (e) {
        e.preventDefault(); // 기본 동작 방지

        const data = {
            title: form.title.value,
            author: form.author.value,
            isbn: form.isbn.value,
            price: form.price.value,
            publishDate: form.publishDate.value
        };

        console.log("Submitting book data:", data);
        alert("Form submitted! Check console log.");
    });
});