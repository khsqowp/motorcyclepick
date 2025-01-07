// CSRF 토큰 가져오기
const token = document.querySelector("meta[name='_csrf']").content;
const header = document.querySelector("meta[name='_csrf_header']").content;

// 콘솔 효과 함수
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

// 이미지 승인 함수
function approveImage(fileName) {
    if (!fileName) {
        addConsoleEffect('Error: 파일 이름이 없습니다.');
        return;
    }

    if(confirm('이 이미지를 승인하시겠습니까?')) {
        addConsoleEffect(fileName + ' 이미지 승인 처리 중...');

        fetch('/admin/approve?fileName=' + encodeURIComponent(fileName), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest',
                [header]: token
            },
            credentials: 'same-origin'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`승인 처리 중 오류가 발생했습니다. (상태 코드: ${response.status})`);
                }
                return response.text();
            })
            .then(result => {
                addConsoleEffect('승인 완료: ' + result);
                setTimeout(() => {
                    location.reload();
                }, 1000);
            })
            .catch(error => {
                console.error('승인 처리 오류:', error);
                addConsoleEffect('오류 발생: ' + error.message);
                alert('오류가 발생했습니다: ' + error.message);
            });
    }
}

// 이미지 거절 함수
function rejectImage(fileName) {
    if (!fileName) {
        addConsoleEffect('Error: 파일 이름이 없습니다.');
        return;
    }

    if(confirm('이 이미지를 거절하시겠습니까?')) {
        addConsoleEffect(fileName + ' 이미지 거절 처리 중...');

        fetch('/admin/reject?fileName=' + encodeURIComponent(fileName), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest',
                [header]: token
            },
            credentials: 'same-origin'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`거절 처리 중 오류가 발생했습니다. (상태 코드: ${response.status})`);
                }
                return response.text();
            })
            .then(result => {
                addConsoleEffect('거절 완료: ' + result);
                setTimeout(() => {
                    location.reload();
                }, 1000);
            })
            .catch(error => {
                console.error('거절 처리 오류:', error);
                addConsoleEffect('오류 발생: ' + error.message);
                alert('오류가 발생했습니다: ' + error.message);
            });
    }
}

// 나머지 코드는 동일...
// 팝업 관련 함수들
function showPopup(imageSrc) {
    const popup = document.getElementById('imagePopup');
    const popupImage = document.getElementById('popupImage');
    popupImage.src = imageSrc;
    popup.style.display = 'flex';
    document.body.style.overflow = 'hidden';
    addConsoleEffect('이미지 미리보기 열기...');
}

function hidePopup() {
    const popup = document.getElementById('imagePopup');
    popup.style.display = 'none';
    document.body.style.overflow = '';
    addConsoleEffect('이미지 미리보기 닫기...');
}

// 이벤트 리스너 설정
document.addEventListener('DOMContentLoaded', function() {
    // 팝업 외부 클릭시 닫기
    const popup = document.getElementById('imagePopup');
    popup.addEventListener('click', function(event) {
        if (event.target === popup) {
            hidePopup();
        }
    });

    // ESC 키로 팝업 닫기
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && popup.style.display === 'flex') {
            hidePopup();
        }
    });
});