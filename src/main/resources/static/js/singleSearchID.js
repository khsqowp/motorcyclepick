document.addEventListener('DOMContentLoaded', function() {
    // Add terminal typing effect to values
    const values = document.querySelectorAll('.value');
    values.forEach(value => {
        const text = value.textContent;
        value.textContent = '';
        let i = 0;
        const typeText = () => {
            if (i < text.length) {
                value.textContent += text.charAt(i);
                i++;
                setTimeout(typeText, 20);
            }
        };
        typeText();
    });

    // Add hover effect to info items
    const infoItems = document.querySelectorAll('.info-item');
    infoItems.forEach(item => {
        item.addEventListener('mouseenter', function() {
            this.style.backgroundColor = 'rgba(0, 255, 0, 0.1)';
            this.style.borderLeftWidth = '4px';
        });

        item.addEventListener('mouseleave', function() {
            this.style.backgroundColor = 'rgba(0, 255, 0, 0.05)';
            this.style.borderLeftWidth = '2px';
        });
    });

    // Add console message when returning to main page
    const backButton = document.querySelector('.btn-secondary');
    backButton.addEventListener('click', function(e) {
        e.preventDefault();
        console.log('Returning to main console...');
        setTimeout(() => {
            window.location.href = this.getAttribute('href');
        }, 500);
    });
});

// Console effect for messages
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