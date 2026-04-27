function deleteArticle(articleId) {
    if (!confirm('Supprimer cet article définitivement ?')) return;

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/admin/articles/${articleId}`, {
        method: 'DELETE',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(res => {
            if (res.ok) {
                const row = document.getElementById('article-row-' + articleId);
                if (row) row.remove();
            } else {
                alert('Erreur lors de la suppression.');
            }
        })
        .catch(() => alert('Erreur réseau.'));
}