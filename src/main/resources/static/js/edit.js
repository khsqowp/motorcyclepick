document.addEventListener('DOMContentLoaded', function() {
    // Add hover effects for input groups
    const inputGroups = document.querySelectorAll('.input-group, .checkbox-group');
    inputGroups.forEach(group => {
        group.addEventListener('mouseenter', function() {
            this.style.backgroundColor = 'rgba(0, 255, 0, 0.1)';
            this.style.borderLeftWidth = '4px';
        });

        group.addEventListener('mouseleave', function() {
            this.style.backgroundColor = 'rgba(0, 255, 0, 0.05)';
            this.style.borderLeftWidth = '2px';
        });
    });

    // Add terminal typing effect to labels
    const labels = document.querySelectorAll('label');
    labels.forEach(label => {
        const text = label.textContent;
        label.textContent = '';
        let i = 0;
        const typeText = () => {
            if (i < text.length) {
                label.textContent += text.charAt(i);
                i++;
                setTimeout(typeText, 20);
            }
        };
        typeText();
    });

    // Form submission
    const form = document.querySelector('.edit-form');
    form.addEventListener('submit', function(e) {
        e.preventDefault();

        console.log('Initializing motorcycle update...');
        setTimeout(() => {
            console.log('Processing form data...');
            setTimeout(() => {
                console.log('Updating motorcycle entry...');
                setTimeout(() => {
                    this.submit();
                }, 500);
            }, 500);
        }, 500);
    });

    // Back button effect
    const backButton = document.querySelector('.btn-secondary');
    backButton.addEventListener('click', function(e) {
        e.preventDefault();
        console.log('Returning to main console...');
        setTimeout(() => {
            window.location.href = this.getAttribute('href');
        }, 500);
    });
});

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