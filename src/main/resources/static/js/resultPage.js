document.addEventListener('DOMContentLoaded', function() {

    const state = {
        currentImageIndex: 1,
        totalImages: 0,
        imagesList: []
    };

    const elements = {
        container: document.querySelector('.container'),
        imageContainer: document.querySelector('.slider-image-container'),
        prevButton: document.getElementById('prevImage'),
        nextButton: document.getElementById('nextImage'),
        imageCounter: document.getElementById('imageCounter'),
        currentImage: document.getElementById('currentImage')
    };

    function getMotorcycleInfo() {
        const container = elements.container;
        if (!container) {
            console.error('Container element not found');
            return null;
        }

        // data-brand 속성과 data-model 속성에서 값을 가져옵니다
        const brand = container.getAttribute('data-brand');
        const model = container.getAttribute('data-model');

        console.log('Found motorcycle info:', { brand, model });
        return { brand, model };
    }

    function initializeImageElement() {
        if (!elements.currentImage) {
            elements.currentImage = document.createElement('img');
            elements.currentImage.id = 'currentImage';
            elements.currentImage.alt = 'Motorcycle Image';
            elements.currentImage.className = 'slider-image';
            elements.imageContainer.appendChild(elements.currentImage);
        }

        elements.currentImage.onerror = function() {
            console.log('Image load error, falling back to no-image');
            this.src = '/static/images/no-image.jpg';
            this.onerror = null;
        };
    }

    async function fetchImages(brand, model) {
        try {
            const formattedBrand = brand.charAt(0).toUpperCase() + brand.slice(1).toLowerCase();
            console.log(`Fetching images for ${formattedBrand} ${model}`);
            const response = await fetch(`/api/images/${formattedBrand}/${model}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const images = await response.json();
            console.log('Fetched images:', images);
            return images;
        } catch (error) {
            console.error('Error fetching images:', error);
            return [];
        }
    }

    function updateImage() {
        if (state.imagesList.length > 0) {
            const info = getMotorcycleInfo();
            if (!info) return;

            const formattedBrand = info.brand.charAt(0).toUpperCase() + info.brand.slice(1).toLowerCase();
            const imagePath = `/static/images/${formattedBrand}/${state.imagesList[state.currentImageIndex - 1]}`;

            console.log('Updating image to:', imagePath);
            elements.currentImage.src = imagePath;
            updateNavigationControls();
        } else {
            console.log('No images available to display');
        }
    }

    function updateNavigationControls() {
        if (elements.prevButton && elements.nextButton && elements.imageCounter) {
            elements.prevButton.style.display = state.totalImages > 1 ? 'block' : 'none';
            elements.nextButton.style.display = state.totalImages > 1 ? 'block' : 'none';
            elements.imageCounter.textContent = `${state.currentImageIndex} / ${state.totalImages}`;
        }
    }

    function setupEventListeners() {
        if (elements.prevButton) {
            elements.prevButton.addEventListener('click', () => {
                if (state.currentImageIndex > 1) {
                    state.currentImageIndex--;
                } else {
                    state.currentImageIndex = state.totalImages;
                }
                updateImage();
            });
        }

        if (elements.nextButton) {
            elements.nextButton.addEventListener('click', () => {
                if (state.currentImageIndex < state.totalImages) {
                    state.currentImageIndex++;
                } else {
                    state.currentImageIndex = 1;
                }
                updateImage();
            });
        }
    }

    function preloadImages() {
        const info = getMotorcycleInfo();
        if (!info) return;

        const formattedBrand = info.brand.charAt(0).toUpperCase() + info.brand.slice(1).toLowerCase();

        state.imagesList.forEach(imageName => {
            const img = new Image();
            img.src = `/images/${formattedBrand}/${imageName}`;
        });
    }

    async function initialize() {
        const info = getMotorcycleInfo();
        if (!info) {
            console.error('Could not get motorcycle info');
            return;
        }

        console.log('Initializing with:', info);

        initializeImageElement();
        setupEventListeners();

        const images = await fetchImages(info.brand, info.model);
        if (images && images.length > 0) {
            state.imagesList = images;
            state.totalImages = images.length;
            updateImage();
            preloadImages();
        }
    }

    // 앱 시작
    initialize().catch(error => {
        console.error('Failed to initialize:', error);
    });
});