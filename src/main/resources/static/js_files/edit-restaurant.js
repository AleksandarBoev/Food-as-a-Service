(() => {
    const elements = {
        editForm: document.getElementById('edit-form'),
        editButton: document.getElementById('edit-restaurant'),
        cancelButton: document.getElementById('cancel-edit'),
    };

    elements.editButton.addEventListener('click', () => {
        elements.editForm.style.display = 'block';
        elements.cancelButton.style.display = 'block';
        elements.editButton.style.display = 'none';
    });

    elements.cancelButton.addEventListener('click', () => {
        elements.editForm.style.display = 'none';
        elements.cancelButton.style.display = 'none';
        elements.editButton.style.display = 'block';
    });
})();