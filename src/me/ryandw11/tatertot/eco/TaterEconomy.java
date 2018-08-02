package me.ryandw11.tatertot.eco;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import me.ryandw11.tatertot.Tatertot;
import me.ryandw11.tatertot.api.NoAccountException;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class TaterEconomy implements Economy{
	private TaterAPI tapi;
	private Tatertot tt;
	public TaterEconomy(Tatertot tt){
		this.tt = tt;
		Vault v = (Vault) Bukkit.getServer().getPluginManager().getPlugin("Vault");
		tapi = new TaterAPI(tt);
		Bukkit.getServer().getPluginManager().registerEvents(new EconomyServerListener(this), v);
		if(tt == null){
			this.tt = tt;
		}
	}

	@Override
	public EconomyResponse bankBalance(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean createPlayerAccount(String p) {
		if(tapi.hasAccount(Bukkit.getOfflinePlayer(p).getUniqueId())){
			return false;
		}
		tapi.createAccount(Bukkit.getOfflinePlayer(p).getUniqueId());
		return true;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer p) {
		if(tapi.hasAccount(p.getUniqueId())){
			return false;
		}
		tapi.createAccount(p.getUniqueId());
		return true;
	}

	@Override
	public boolean createPlayerAccount(String arg0, String arg1) {
		return createPlayerAccount(arg0);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		return createPlayerAccount(arg0);
	}

	@Override
	public String currencyNamePlural() {
		return "Tatertots";
	}

	@Override
	public String currencyNameSingular() {
		return "Tatertot";
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse depositPlayer(String p, double amount) {
		 if (amount < 0) {
	            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot desposit negative funds");
	     }
		 int balance = 0;
		 EconomyResponse.ResponseType type;
		 tapi.addBalance(Bukkit.getOfflinePlayer(p).getUniqueId(), amount);
		 String error = null;
		 try {
			balance = tapi.getBalance(Bukkit.getOfflinePlayer(p).getUniqueId());
			type = ResponseType.SUCCESS;
		} catch (NoAccountException e) {
			type = ResponseType.FAILURE;
			error = "That account does not exist!";
			amount = 0;
		}
		 return new EconomyResponse(amount, balance, type, error);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer op, double amount) {
		return depositPlayer(op.getName(), amount);
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		return depositPlayer(arg0, arg2);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		return depositPlayer(arg0.getName(), arg2);
	}

	@Override
	public String format(double arg0) {
		DecimalFormat twoDPlaces = new DecimalFormat("#,###");
		twoDPlaces.setRoundingMode(RoundingMode.HALF_UP);
		return twoDPlaces.format(arg0);
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getBalance(String p) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(p);
		try {
			return tapi.getBalance(op.getUniqueId());
		} catch (NoAccountException e) {
			return 0;
		}
	}

	@Override
	public double getBalance(OfflinePlayer op) {
		try {
			return tapi.getBalance(op.getUniqueId());
		} catch (NoAccountException e) {
			return 0;
		}
	}

	@Override
	public double getBalance(String p, String world) {
		return getBalance(p);
	}

	@Override
	public double getBalance(OfflinePlayer op, String world) {
		return getBalance(op);
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<String>();
	}

	@Override
	public String getName() {
		return "TaterTots Eco";
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean has(String arg0, double arg1) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(arg0);
		try {
			if(tapi.getBalance(op.getUniqueId()) >= arg1)
				return true;
		} catch (NoAccountException e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean has(OfflinePlayer op, double amount) {
		try {
			if(tapi.getBalance(op.getUniqueId()) >= amount)
				return true;
		} catch (NoAccountException e) {
			return false;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean has(String p, String world, double amount) {
		OfflinePlayer op = Bukkit.getOfflinePlayer(p);
		try {
			if(tapi.getBalance(op.getUniqueId()) >= amount)
				return true;
		} catch (NoAccountException e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean has(OfflinePlayer op, String world, double amount) {
		try {
			if(tapi.getBalance(op.getUniqueId()) >= amount)
				return true;
		} catch (NoAccountException e) {
			return false;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean hasAccount(String arg0) {
		return tapi.hasAccount(Bukkit.getOfflinePlayer(arg0).getUniqueId());
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		return tapi.hasAccount(arg0.getUniqueId());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean hasAccount(String arg0, String arg1) {
		return tapi.hasAccount(Bukkit.getOfflinePlayer(arg0).getUniqueId());
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		return tapi.hasAccount(arg0.getUniqueId());
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Tatertots Eco does not support bank accounts!");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public EconomyResponse withdrawPlayer(String p, double amount) {
		 if (amount < 0) {
	            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative funds");
	     }
		 int balance;
	     EconomyResponse.ResponseType type;
	     String errorMessage = null;
	     tapi.subtractBalance(Bukkit.getOfflinePlayer(p).getUniqueId(), amount);
	     try {
			balance = tapi.getBalance(Bukkit.getOfflinePlayer(p).getUniqueId());
			type = ResponseType.SUCCESS;
		} catch (NoAccountException e) {
			type = ResponseType.FAILURE;
			balance = 0;
			amount = 0;
			errorMessage = "Account not found.";
		}
	     
	     return new EconomyResponse(amount, balance, type, errorMessage);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
		return withdrawPlayer(arg0.getName(), arg1);
	}

	@Override
	public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
		return withdrawPlayer(arg0, arg2);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		return withdrawPlayer(arg0.getName(), arg2);
	}
	
	public class EconomyServerListener implements Listener {
        TaterEconomy economy = null;

        public EconomyServerListener(TaterEconomy economy) {
            this.economy = economy;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (economy.tt == null) {
                Plugin essentials = event.getPlugin();

                if (essentials.getDescription().getName().equals("TatertotEco")) {
                    economy.tt = economy.tt;
                    tt.getLogger().info("[TatertotEco] Tatertot eco has hooked into vault!");
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (economy.tt != null) {
                if (event.getPlugin().getDescription().getName().equals("TatertotEco")) {
                    economy.tt = null;
                }
            }
        }
    }

}
