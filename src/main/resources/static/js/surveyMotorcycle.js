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
            nextQuestion.style.opacity = '1';
            nextQuestion.style.transform = 'translateY(0)';
        }, 300);
    }
}

function handleCostSubmit() {
    const costInput = document.getElementById('costInput');
    const costInputHidden = document.getElementById('question1Input');
    const errorDiv = document.getElementById('budgetError');
    const cost = parseInt(costInput.value);

    if (!cost || cost <= 0 || isNaN(cost)) {
        errorDiv.style.display = 'block';
        return false;
    }

    const costValue = cost.toString();
    answers.question1 = costValue;
    if (costInputHidden) {
        costInputHidden.value = costValue;
    }

    errorDiv.style.display = 'none';

    const currentQuestion = document.getElementById('q1');
    const nextQuestion = document.getElementById('q2');
    transitionQuestion(currentQuestion, nextQuestion);
    updateProgress(2);
}

function selectOption(questionNum, value) {
    answers[`question${questionNum}`] = value;
    const hiddenInput = document.getElementById(`question${questionNum}Input`);
    if (hiddenInput) {
        hiddenInput.value = value;
    }

    const errorDiv = document.getElementById(`q${questionNum}Error`);
    if (errorDiv) {
        errorDiv.style.display = 'none';
    }

    const currentQuestion = document.getElementById(`q${questionNum}`);
    const nextQuestion = document.getElementById(`q${questionNum + 1}`);
    transitionQuestion(currentQuestion, nextQuestion);
    updateProgress(questionNum + 1);
}

function toggleSelect(element, value) {
    element.style.transition = 'all 0.3s ease';
    element.classList.toggle('selected');
    const index = selectedItems.indexOf(value);
    const errorDiv = document.getElementById('q7Error');
    const hiddenInput = document.getElementById('question7Input');

    if (index === -1) {
        selectedItems.push(value);
    } else {
        selectedItems.splice(index, 1);
    }

    answers.question7 = selectedItems;
    if (hiddenInput) {
        hiddenInput.value = selectedItems.join(',');
    }

    errorDiv.style.display = selectedItems.length > 0 ? 'none' : 'block';

    const finalSubmitBtn = document.getElementById('finalSubmitBtn');
    if (finalSubmitBtn) {
        finalSubmitBtn.style.transition = 'opacity 0.3s ease';
        finalSubmitBtn.style.display = selectedItems.length > 0 ? 'block' : 'none';
        if (selectedItems.length > 0) {
            setTimeout(() => {
                finalSubmitBtn.style.opacity = '1';
            }, 50);
        }
    }
}

function updateProgress(step) {
    const progress = ((step - 1) / 6) * 100;
    const progressFill = document.getElementById('progressFill');
    const currentStep = document.getElementById('currentStep');

    if (progressFill) {
        progressFill.style.transition = 'width 0.3s ease';
        progressFill.style.width = `${progress}%`;
    }
    if (currentStep) {
        currentStep.style.transition = 'opacity 0.3s ease';
        currentStep.style.opacity = '0';
        setTimeout(() => {
            currentStep.textContent = step;
            currentStep.style.opacity = '1';
        }, 150);
    }
}

function submitSurvey(event) {
    if (event) {
        event.preventDefault();
    }

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

    const form = document.querySelector('form');
    if (form) {
        form.submit();
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const costInput = document.getElementById('costInput');
    if (costInput) {
        costInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                handleCostSubmit();
            }
        });
    }

    const completeButton = document.querySelector('#q1 button');
    if (completeButton) {
        completeButton.addEventListener('click', handleCostSubmit);
    }

    const errorDivs = document.querySelectorAll('.error');
    errorDivs.forEach(div => div.style.display = 'none');

    const firstQuestion = document.getElementById('q1');
    if (firstQuestion) {
        setTimeout(() => {
            firstQuestion.style.opacity = '1';
            firstQuestion.style.transform = 'translateY(0)';
        }, 100);
    }

    const options = document.querySelectorAll('.option-container > *');
    options.forEach(option => {
        option.style.transition = 'all 0.3s ease';
    });

    updateProgress(1);
});