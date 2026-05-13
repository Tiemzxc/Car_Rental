document.addEventListener('DOMContentLoaded', ()=>{
    const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
    const registerModal = new bootstrap.Modal(document.getElementById('registerModal'));
    const bookingModal = new bootstrap.Modal(document.getElementById('bookingModal'));

    document.getElementById('openLogin').addEventListener('click', ()=>loginModal.show());
    document.getElementById('openRegister').addEventListener('click', ()=>registerModal.show());
    document.getElementById('scrollToFleet').addEventListener('click', ()=>document.getElementById('fleet').scrollIntoView({behavior:'smooth'}));

    document.getElementById('contactForm').addEventListener('submit', e=>{
        e.preventDefault();
        const name=document.getElementById('contactName').value.trim();
        const email=document.getElementById('contactEmail').value.trim();
        const message=document.getElementById('contactMessage').value.trim();
        if(!name||!email||!message){alert('Please fill all fields');return;}
        showToast('Thank you for your query. We will contact you soon.');
        document.getElementById('contactForm').reset();
    });
});

function openBookingModal(button){
    const id=button.getAttribute('data-id');
    const model=button.getAttribute('data-model');
    document.getElementById('bkVehicleId').value=id;
    document.getElementById('bkVehicleModel').value=model;
    const bookingModal=new bootstrap.Modal(document.getElementById('bookingModal'));
    bookingModal.show();
}

function showToast(message){
    const toastEl=document.createElement('div');
    toastEl.className='toast align-items-center text-bg-primary border-0 toast-custom';
    toastEl.innerHTML=`<div class="d-flex"><div class="toast-body">${message}</div><button type="button" class="btn-close btn-close-white me-2 m-auto" onclick="this.closest('.toast').remove()"></button></div>`;
    document.body.appendChild(toastEl);
    const toast = new bootstrap.Toast(toastEl,{delay:2000});
    toast.show();
    toastEl.addEventListener('hidden.bs.toast', ()=>toastEl.remove());
}
