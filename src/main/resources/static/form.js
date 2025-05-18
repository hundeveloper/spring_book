// form.js
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("book-form");
    const errorMessage = document.getElementById("error-message");
    const tableBody = document.querySelector("#book-table tbody");
    let editId = null;

    const API_BASE = "/api/books";

    function loadBooks() {
        fetch(API_BASE)
            .then(res => res.json())
            .then(data => {
                tableBody.innerHTML = "";
                data.forEach(book => {
                    const row = document.createElement("tr");
                    row.innerHTML = `
            <td>${book.id}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.isbn}</td>
            <td>${book.price}</td>
            <td>${book.publishDate}</td>
            <td><button onclick="editBook(${book.id})">수정</button></td>
            <td><button onclick="deleteBook(${book.id})">삭제</button></td>
          `;
                    tableBody.appendChild(row);
                });
            });
    }

    window.editBook = function(id) {
        fetch(`${API_BASE}/${id}`)
            .then(res => res.json())
            .then(book => {
                editId = book.id;
                form.title.value = book.title;
                form.author.value = book.author;
                form.isbn.value = book.isbn;
                form.price.value = book.price;
                form.publishDate.value = book.publishDate;
                if (book.detail) {
                    form.description.value = book.detail.description || "";
                    form.language.value = book.detail.language || "";
                    form.pageCount.value = book.detail.pageCount || 0;
                    form.publisher.value = book.detail.publisher || "";
                    form.coverImageUrl.value = book.detail.coverImageUrl || "";
                    form.edition.value = book.detail.edition || "";
                }
            });
    }

    window.deleteBook = function(id) {
        if (!confirm("정말 삭제하시겠습니까?")) return;
        fetch(`${API_BASE}/${id}`, { method: "DELETE" })
            .then(() => loadBooks());
    }

    form.addEventListener("submit", e => {
        e.preventDefault();
        errorMessage.textContent = "";

        const data = {
            title: form.title.value.trim(),
            author: form.author.value.trim(),
            isbn: form.isbn.value.trim(),
            price: parseInt(form.price.value),
            publishDate: form.publishDate.value,
            detailRequest: {
                description: form.description.value,
                language: form.language.value,
                pageCount: parseInt(form.pageCount.value || 0),
                publisher: form.publisher.value,
                coverImageUrl: form.coverImageUrl.value,
                edition: form.edition.value
            }
        };

        const method = editId ? "PUT" : "POST";
        const url = editId ? `${API_BASE}/${editId}` : API_BASE;

        fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
            .then(res => {
                if (!res.ok) return res.json().then(err => { throw err; });
                return res.json();
            })
            .then(() => {
                form.reset();
                editId = null;
                loadBooks();
            })
            .catch(err => {
                errorMessage.textContent = err.message || "에러가 발생했습니다.";
            });
    });

    loadBooks();
});