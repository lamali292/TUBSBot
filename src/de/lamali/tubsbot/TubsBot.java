package de.lamali.tubsbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import de.lamali.tubsbot.listener.CommandListener;
import de.lamali.tubsbot.reactionroles.ReactionGroupManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

public class TubsBot {
public static TubsBot INSTANCE;
	
	public ShardManager shardMan;
	private CommandManager cmdMan;
	private ReactionGroupManager groupMan;
	private AmazonS3 s3;
	public static void main(String[] args) {
		try {
			new TubsBot();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public TubsBot() throws LoginException, IllegalArgumentException {
			INSTANCE = this;
			
			
			ArrayList<GatewayIntent> intents = new ArrayList<>();
			intents.add(GatewayIntent.GUILD_MESSAGE_REACTIONS);
			intents.add(GatewayIntent.GUILD_MESSAGES);
			
			DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.create(intents);
			builder.setToken(Contants.JDA_TOKEN);

			builder.setActivity(Activity.watching("nach Reaktionen"));
			builder.setStatus(OnlineStatus.ONLINE);
						
			this.cmdMan = new CommandManager();
			
			builder.addEventListeners(new CommandListener());
			this.shardMan = builder.build();
			
			AWSCredentials credentials = new BasicAWSCredentials(Contants.AWS_ACCESS_KEY_ID, Contants.AWS_SECRET_ACCESS_KEY);
			s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.EU_CENTRAL_1).build();
			
			this.groupMan = new ReactionGroupManager();
						
			System.out.println("Bot online");
			
			//shutdown();

	}
	
	public void shutdown() {
		new Thread(() -> {
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while ((line = reader.readLine()) != null) {
					if(line.equalsIgnoreCase("exit")) {
						if(shardMan != null) {
							shardMan.setStatus(OnlineStatus.OFFLINE);
							shardMan.shutdown();
							System.out.println("Bot offline");
						}
						
						reader.close();
					}else {
						System.out.println("Use 'exit' to shutdown");
					}
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}).start();

	}

	public CommandManager getCmdMan() {
		return cmdMan;
	}

	public ReactionGroupManager getGroupMan() {
		return groupMan;
	}

	public AmazonS3 getAWSS3() {
		return s3;
	}

}
