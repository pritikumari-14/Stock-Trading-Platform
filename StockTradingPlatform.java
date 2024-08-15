import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;

public class StockTradingPlatform {

    static class Stock {
        String symbol;
        double price;

        public Stock(String symbol, double price) {
            this.symbol = symbol;
            this.price = price;
        }
        public void updatePrice() {
            this.price *= (0.95 + Math.random() * 0.1);
        }
    }

    static class Portfolio {
        double balance;
        Map<String, Integer> holdings;
        Map<LocalDate, Double> performanceHistory;

        public Portfolio(double balance) {
            this.balance = balance;
            this.holdings = new HashMap<>();
            this.performanceHistory = new HashMap<>();
        }

        public void buyStock(String symbol, int quantity, double price) {
            if (balance >= price * quantity) {
                balance -= price * quantity;
                holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
                System.out.println("Bought " + quantity + " shares of " + symbol);
            } else {
                System.out.println("Insufficient funds to buy " + quantity + " shares of " + symbol);
            }
        }

        public void sellStock(String symbol, int quantity, double price) {
            if (holdings.containsKey(symbol) && holdings.get(symbol) >= quantity) {
                balance += price * quantity;
                holdings.put(symbol, holdings.get(symbol) - quantity);
                if (holdings.get(symbol) == 0) {
                    holdings.remove(symbol);
                }
                System.out.println("Sold " + quantity + " shares of " + symbol);
            } else {
                System.out.println("Insufficient shares to sell " + quantity + " shares of " + symbol);
            }
        }

        public void viewPortfolio(Map<String, Stock> marketData) {
            System.out.println("Current Portfolio:");
            double totalValue = balance;
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                String symbol = entry.getKey();
                int shares = entry.getValue();
                double stockValue = shares * marketData.get(symbol).price;
                totalValue += stockValue;
                System.out.println(symbol + ": " + shares + " shares, Current Value: $" + stockValue);
            }
            System.out.println("Account Balance: $" + balance);
            System.out.println("Total Portfolio Value: $" + totalValue);
            updatePerformanceHistory(totalValue);
        }

        private void updatePerformanceHistory(double totalValue) {
            LocalDate today = LocalDate.now();
            performanceHistory.put(today, totalValue);
        }

        public void viewPerformance() {
            System.out.println("Performance History:");
            for (Map.Entry<LocalDate, Double> entry : performanceHistory.entrySet()) {
                System.out.println(entry.getKey() + ": $" + entry.getValue());
            }

            if (performanceHistory.size() > 1) {
                LocalDate firstDate = performanceHistory.keySet().iterator().next();
                double initialValue = performanceHistory.get(firstDate);
                LocalDate lastDate = performanceHistory.keySet().stream().reduce((first , second) -> second).orElse(firstDate);
                double currentValue = performanceHistory.get(lastDate);
                double returnPercentage = ((currentValue - initialValue) / initialValue) * 100;
                System.out.println("Overall Return: " + String.format("%.2f", returnPercentage) + "%");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        Map<String, Stock> marketData = new HashMap<>();
        marketData.put("APPLE", new Stock("APPLE", 150.00));
        marketData.put("GOOGLE", new Stock("GOOGLE", 2800.00));
        marketData.put("AMAZON", new Stock("AMAZON", 3400.00));
        marketData.put("MSFT", new Stock("MSFT", 300.00));


        Portfolio portfolio = new Portfolio(10000.00);

        while (true) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Performance");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // View Market Data
                    System.out.println("Market Data:");
                    for (Stock stock : marketData.values()) {
                        stock.updatePrice(); // Simulate price change
                        System.out.println(stock.symbol + ": $" + String.format("%.2f", stock.price));
                    }
                    break;

                case 2:
                    // Buy Stock
                    System.out.print("Enter stock symbol to buy: ");
                    String buySymbol = scanner.next().toUpperCase();
                    if (marketData.containsKey(buySymbol)) {
                        System.out.print("Enter quantity to buy: ");
                        int buyQuantity = scanner.nextInt();
                        portfolio.buyStock(buySymbol, buyQuantity, marketData.get(buySymbol).price);
                    } else {
                        System.out.println("Invalid stock symbol.");
                    }
                    break;

                case 3:
                    // Sell Stock
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.next().toUpperCase();
                    if (portfolio.holdings.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity to sell: ");
                        int sellQuantity = scanner.nextInt();
                        portfolio.sellStock(sellSymbol, sellQuantity, marketData.get(sellSymbol).price);
                    } else {
                        System.out.println("You do not own any shares of " + sellSymbol);
                    }
                    break;

                case 4:
                    // View Portfolio
                    portfolio.viewPortfolio(marketData);
                    break;

                case 5:
                    // View Performance
                    portfolio.viewPerformance();
                    break;

                case 6:
                    // Exit
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}