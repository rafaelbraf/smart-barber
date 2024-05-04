document.addEventListener("DOMContentLoaded", function() {
    const ctx1 = document.getElementById('grafico1').getContext('2d');
    new Chart(ctx1, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
            datasets: [{
                label: 'Vendas',
                data: [30, 40, 35, 50, 45],
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                tension: 0.5
            }]
        }
    });

    const ctx2 = document.getElementById('grafico2').getContext('2d');
    new Chart(ctx2, {
        type: 'bar',
        data: {
            labels: ['Corte', 'Corte degradê', 'Barba'],
            datasets: [{
                label: 'Vendas',
                data: [50, 25, 15],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)'
                ],
                borderWidth: 1
            }]
        }
    });
});