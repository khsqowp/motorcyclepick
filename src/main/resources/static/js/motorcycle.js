function addConsoleEffect(message) {
    const chars = message.split('');
    let output = '';
    let i = 0;
    const interval = setInterval(() => {
        if (i < chars.length) {
            output += chars[i];
            console.log(`${output}_`);
            i++;
        } else {
            clearInterval(interval);
        }
    }, 50);
}

function viewMotorcycle() {
    const id = document.getElementById('viewId').value;
    if (!id) {
        addConsoleEffect('Error: ID를 입력해주세요.');
        return;
    }
    addConsoleEffect('Searching for Motorcycle ' + id + '...');
    setTimeout(() => {
        window.location.href = `/motorcycle/singleSearchID?id=${id}`;
    }, 500);
}

function editMotorcycle() {
    const id = document.getElementById('editId').value;
    if (!id) {
        addConsoleEffect('Error: ID를 입력해주세요.');
        return;
    }
    addConsoleEffect('Loading edit mode for Motorcycle ' + id + '...');
    setTimeout(() => {
        window.location.href = `/motorcycle/edit?editId=${id}`;
    }, 500);
}

function deleteMotorcycle() {
    const id = document.getElementById('deleteId').value;
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/motorcycle/delete';

    // CSRF 토큰 추가
    const csrfToken = document.querySelector("meta[name='_csrf']").content;
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").content;

    const idInput = document.createElement('input');
    idInput.type = 'hidden';
    idInput.name = 'motorcycleID';
    idInput.value = id;

    const csrfInput = document.createElement('input');
    csrfInput.type = 'hidden';
    csrfInput.name = '_csrf';
    csrfInput.value = csrfToken;

    form.appendChild(idInput);
    form.appendChild(csrfInput);
    document.body.appendChild(form);
    form.submit();
}

// 키보드 이벤트 처리
document.addEventListener('DOMContentLoaded', function() {
    const inputs = document.querySelectorAll('.input-group input');

    // 입력 필드 포커스 효과
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.style.boxShadow = '0 0 15px rgba(0, 255, 0, 0.3)';
        });

        input.addEventListener('blur', function() {
            this.parentElement.style.boxShadow = 'none';
        });

        // Enter 키 이벤트
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                this.nextElementSibling.click();
            }
        });
    });
});