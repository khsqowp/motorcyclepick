// 전역 변수 설정
let selectedItems = [];
let answers = {
    question1: '',
    question2: '',
    question3: '',
    question4: '',
    question5: '',
    question6: '',
    question7: []
};

// 문항 전환 애니메이션 함수
function transitionQuestion(currentQuestion, nextQuestion) {
    if (currentQuestion && nextQuestion) {
        currentQuestion.style.opacity = '0';
        currentQuestion.style.transform = 'translateY(20px)';

        setTimeout(() => {
            currentQuestion.classList.remove('active');
            nextQuestion.classList.add('active');

            setTimeout(() => {
                nextQuestion.style.opacity = '1';
                nextQuestion.style.transform = 'translateY(0)';
            }, 50);
        }, 300);
    }
}

// 비용 입력 처리 함수
function handleCostSubmit() {
    const costInput = document.getElementById('costInput');
    const costInputHidden = document.getElementById('question1Input');
    const errorDiv = document.getElementById('budgetError');
    const cost = parseInt(costInput.value);

    if (!cost || cost <= 0 || isNaN(cost)) {
        errorDiv.style.display = 'block';
        costInput.classList.add('error-input');
        return false;
    }

    const costValue = cost.toString();
    answers.question1 = costValue;
    if (costInputHidden) {
        costInputHidden.value = costValue;
    }

    errorDiv.style.display = 'none';
    costInput.classList.remove('error-input');

    const currentQuestion = document.getElementById('q1');
    const nextQuestion = document.getElementById('q2');
    transitionQuestion(currentQuestion, nextQuestion);
    updateProgress(2);
}

// 옵션 선택 처리 함수
function selectOption(questionNum, value) {
    // 이전 선택된 옵션의 스타일 초기화
    const prevSelected = document.querySelector(`#q${questionNum} .option-btn.selected`);
    if (prevSelected) {
        prevSelected.classList.remove('selected');
    }

    // 현재 선택된 버튼에 스타일 적용
    const selectedBtn = event.target;
    selectedBtn.classList.add('selected');

    answers[`question${questionNum}`] = value;
    const hiddenInput = document.getElementById(`question${questionNum}Input`);
    if (hiddenInput) {
        hiddenInput.value = value;
    }

    const errorDiv = document.getElementById(`q${questionNum}Error`);
    if (errorDiv) {
        errorDiv.style.display = 'none';
    }

    // 애니메이션과 함께 다음 질문으로 전환
    setTimeout(() => {
        const currentQuestion = document.getElementById(`q${questionNum}`);
        const nextQuestion = document.getElementById(`q${questionNum + 1}`);
        transitionQuestion(currentQuestion, nextQuestion);
        updateProgress(questionNum + 1);
    }, 300);
}

// 다중 선택 토글 함수
function toggleSelect(element, value) {
    const errorDiv = document.getElementById('q7Error');
    const hiddenInput = document.getElementById('question7Input');

    if (element.classList.contains('selected')) {
        // 선택 해제
        element.classList.remove('selected');
        const index = selectedItems.indexOf(value);
        if (index !== -1) {
            selectedItems.splice(index, 1);
        }
    } else {
        // 새로운 선택
        if (selectedItems.length >= 2) {
            // 이미 2개가 선택된 경우, 첫 번째 선택 항목 제거
            const firstSelected = document.querySelector('.select-item.selected');
            if (firstSelected) {
                firstSelected.classList.remove('selected');
                selectedItems.shift();
            }
        }
        element.classList.add('selected');
        selectedItems.push(value);
    }

    // 입력값 업데이트
    answers.question7 = selectedItems;
    if (hiddenInput) {
        hiddenInput.value = selectedItems.join(',');
    }

    // 에러 메시지와 제출 버튼 표시 상태 업데이트
    errorDiv.style.display = selectedItems.length > 0 ? 'none' : 'block';
    const finalSubmitBtn = document.getElementById('finalSubmitBtn');
    if (finalSubmitBtn) {
        finalSubmitBtn.style.display = selectedItems.length > 0 ? 'block' : 'none';
    }
}

// 진행률 업데이트 함수
function updateProgress(step) {
    const progress = ((step - 1) / 6) * 100;
    const progressFill = document.getElementById('progressFill');
    const currentStep = document.getElementById('currentStep');

    if (progressFill) {
        progressFill.style.width = `${progress}%`;
    }

    if (currentStep) {
        currentStep.textContent = step;
    }
}

// 설문 제출 함수
function submitSurvey(event) {
    if (event) {
        event.preventDefault();
    }

    // 모든 답변 검증
    let isValid = true;
    for (let i = 1; i <= 6; i++) {
        const answer = answers[`question${i}`];
        if (!answer || answer.trim() === '') {
            const errorDiv = document.getElementById(`q${i}Error`);
            if (errorDiv) {
                errorDiv.style.display = 'block';
            }
            document.getElementById(`q${i}`).classList.add('active');
            isValid = false;
            break;
        }
    }

    // 마지막 질문(다중 선택) 검증
    if (selectedItems.length === 0) {
        const errorDiv = document.getElementById('q7Error');
        if (errorDiv) {
            errorDiv.style.display = 'block';
        }
        isValid = false;
    }

    if (!isValid) {
        return false;
    }

    // 폼 제출
    const form = document.querySelector('form');
    if (form) {
        form.submit();
    }
}

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 비용 입력 필드에 엔터 키 이벤트 리스너 추가
    const costInput = document.getElementById('costInput');
    if (costInput) {
        costInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                handleCostSubmit();
            }
        });

        // 자동 포커스
        costInput.focus();
    }

    // 에러 메시지 초기화
    const errorDivs = document.querySelectorAll('.error');
    errorDivs.forEach(div => div.style.display = 'none');

    // 첫 번째 질문 애니메이션
    const firstQuestion = document.getElementById('q1');
    if (firstQuestion) {
        setTimeout(() => {
            firstQuestion.style.opacity = '1';
            firstQuestion.style.transform = 'translateY(0)';
        }, 100);
    }

    // 진행률 초기화
    updateProgress(1);

    // 옵션 버튼에 호버 효과 추가
    const options = document.querySelectorAll('.option-btn, .select-item');
    options.forEach(option => {
        option.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
        });
        option.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
});