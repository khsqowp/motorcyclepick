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
    if (!id) {
        addConsoleEffect('Error: ID를 입력해주세요.');
        return;
    }

    if (confirm('[WARNING] 정말로 이 Motorcycle을 삭제하시겠습니까?')) {
        addConsoleEffect('Initiating delete sequence for Motorcycle ' + id + '...');
        setTimeout(() => {
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/motorcycle/delete';

            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'motorcycleID';
            input.value = id;

            form.appendChild(input);
            document.body.appendChild(form);
            form.submit();
        }, 500);
    }
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