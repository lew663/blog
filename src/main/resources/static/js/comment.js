document.addEventListener("DOMContentLoaded", function() {
    const articleId = /*[[${article.id}]]*/ '0'; // Thymeleaf로 article.id를 가져옵니다.

    // 댓글 작성
    document.getElementById('submitComment').addEventListener('click', function() {
        const commentContent = document.getElementById('commentContent').value;

        fetch(`/comment`, {
            method: 'POST',
            body: JSON.stringify({
                articleId: articleId,
                content: commentContent,
                parentId: null // 댓글 작성 시 parentId는 null로 설정
            }),
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            credentials: 'include'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('댓글 작성 실패');
            }
            return response.json();
        })
        .then(data => {
            const commentList = document.querySelector('.comment-list');
            const newComment = document.createElement('div');
            newComment.className = 'comment-item';
            newComment.innerHTML = `
                <p>${data.content}</p>
                <small>${data.email}</small>
                <button class="btn btn-link reply-btn" data-id="${data.id}">대댓글</button>
                <div class="reply-form" style="display: none;">
                    <label>
                        <textarea class="form-control" placeholder="대댓글을 입력하세요..." data-parent-id="${data.id}"></textarea>
                    </label>
                    <button class="btn btn-secondary submit-reply">등록</button>
                </div>
            `;
            commentList.prepend(newComment);
            document.getElementById('commentContent').value = ''; // 입력창 초기화
        })
        .catch(error => console.error('Error:', error));
    });

    // 대댓글 버튼 클릭 시 대댓글 폼 토글
    document.querySelectorAll('.reply-btn').forEach(button => {
        button.addEventListener('click', function() {
            const replyForm = this.nextElementSibling;
            replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
        });
    });

    // 대댓글 작성
    document.querySelectorAll('.submit-reply').forEach(button => {
        button.addEventListener('click', function() {
            const replyForm = this.previousElementSibling.querySelector('textarea');
            const parentId = replyForm.getAttribute('data-parent-id');

            fetch(`/comment`, {
                method: 'POST',
                body: JSON.stringify({
                    articleId: articleId,
                    content: replyForm.value,
                    parentId: parentId
                }),
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'include'
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('대댓글 작성 실패');
                }
                return response.json();
            })
            .then(data => {
                const newChildComment = document.createElement('div');
                newChildComment.className = 'child-comment';
                newChildComment.innerHTML = `
                    <p>${data.content}</p>
                    <small>${data.email}</small>
                `;
                this.parentElement.parentElement.appendChild(newChildComment);
                replyForm.value = ''; // 대댓글 입력창 초기화
            })
            .catch(error => console.error('Error:', error));
        });
    });
});
