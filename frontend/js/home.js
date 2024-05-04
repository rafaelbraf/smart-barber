function popularTabelaServicos() {
    const dados = [
        { nome: 'Corte de Cabelo', data: '2024-05-01', valorTotal: 50.0, duracao: '30 minutos' },
        { nome: 'Barba', data: '2024-05-02', valorTotal: 30.0, duracao: '20 minutos' },
        { nome: 'Corte e Barba', data: '2024-05-03', valorTotal: 70.0, duracao: '45 minutos' },
        { nome: 'Coloração', data: '2024-05-04', valorTotal: 80.0, duracao: '60 minutos' },
        { nome: 'Hidratação', data: '2024-05-05', valorTotal: 90.0, duracao: '40 minutos' },
    ];

    const tbody = document.getElementById('tabela-servicos'); // Obter o corpo da tabela

    dados.forEach(servico => {
        // Criar uma nova linha
        const row = document.createElement('tr');

        // Criar células para cada coluna e adicionar os dados
        const nomeCell = document.createElement('td');
        nomeCell.textContent = servico.nome;

        const dataCell = document.createElement('td');
        dataCell.textContent = servico.data;

        const valorCell = document.createElement('td');
        valorCell.textContent = `R$ ${servico.valorTotal.toFixed(2)}`; // Formatando o valor com duas casas decimais

        const duracaoCell = document.createElement('td');
        duracaoCell.textContent = servico.duracao;

        // Adicionar as células à linha
        row.appendChild(nomeCell);
        row.appendChild(dataCell);
        row.appendChild(valorCell);
        row.appendChild(duracaoCell);

        // Adicionar a linha ao corpo da tabela
        tbody.appendChild(row);
    });
}

// Chamar a função para popular a tabela ao carregar a página
document.addEventListener('DOMContentLoaded', popularTabelaServicos);