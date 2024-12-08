document.addEventListener('DOMContentLoaded', function() {
    // 스크롤 애니메이션
    const specItems = document.querySelectorAll('.spec-item');

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
            }
        });
    }, {
        threshold: 0.1
    });

    specItems.forEach(item => {
        observer.observe(item);
        item.style.opacity = '0';
        item.style.transform = 'translateY(20px)';
    });

    // 통계 카드 호버 효과
    const statCards = document.querySelectorAll('.stat-card');

    statCards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.style.transform = 'translateY(-5px)';
            card.style.transition = 'transform 0.2s ease';
        });

        card.addEventListener('mouseleave', () => {
            card.style.transform = 'translateY(0)';
        });
    });

    // 네비게이션 버튼 스무스 효과
    const navBtns = document.querySelectorAll('.nav-btn');

    navBtns.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const href = btn.getAttribute('href');

            // 페이지 전환 애니메이션
            document.body.style.opacity = '0';
            document.body.style.transition = 'opacity 0.3s ease';

            setTimeout(() => {
                window.location.href = href;
            }, 300);
        });
    });
    const pageInput = document.querySelector('.page-input');
    if (pageInput) {
        pageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const pageNumber = parseInt(this.value);
                const maxPage = parseInt(this.getAttribute('max'));

                if (pageNumber && pageNumber >= 1 && pageNumber <= maxPage) {
                    // 페이지 전환 애니메이션
                    document.body.style.opacity = '0';
                    document.body.style.transition = 'opacity 0.3s ease';

                    setTimeout(() => {
                        // index는 0부터 시작하므로 1을 빼줍니다
                        window.location.href = `/results?index=${pageNumber - 1}`;
                    }, 300);
                } else {
                    alert('유효한 페이지 번호를 입력해주세요.');
                    this.value = this.defaultValue;
                }
            }
        });

        // 입력값 유효성 검사
        pageInput.addEventListener('input', function() {
            const maxPage = parseInt(this.getAttribute('max'));
            let value = parseInt(this.value);

            if (value > maxPage) {
                this.value = maxPage;
            } else if (value < 1) {
                this.value = 1;
            }
        });
    }
});

// CSS 애니메이션을 위한 스타일 추가
const style = document.createElement('style');
style.textContent = `
  .fade-in {
    animation: fadeIn 0.5s ease forwards;
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`;
document.head.appendChild(style);