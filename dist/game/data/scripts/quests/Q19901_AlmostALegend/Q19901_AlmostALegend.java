/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q19901_AlmostALegend;

import org.l2jmobius.gameserver.data.xml.TeleportListData;
import org.l2jmobius.gameserver.enums.CategoryType;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.events.EventType;
import org.l2jmobius.gameserver.model.events.ListenerRegisterType;
import org.l2jmobius.gameserver.model.events.annotations.RegisterEvent;
import org.l2jmobius.gameserver.model.events.annotations.RegisterType;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerLevelChanged;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerLogin;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerProfessionChange;
import org.l2jmobius.gameserver.model.events.impl.creature.player.OnPlayerQuestAccept;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestDialogType;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.newquestdata.NewQuestLocation;
import org.l2jmobius.gameserver.model.quest.newquestdata.QuestCondType;
import org.l2jmobius.gameserver.network.serverpackets.quest.ExQuestDialog;
import org.l2jmobius.gameserver.network.serverpackets.quest.ExQuestNotification;

import quests.Q10352_StopTheLizardmen1.Q10352_StopTheLizardmen1;

/**
 * @author Magik
 */
public class Q19901_AlmostALegend extends Quest
{
	private static final int QUEST_ID = 19901;
	
	public Q19901_AlmostALegend()
	{
		super(QUEST_ID);
	}
	
	@Override
	public String onEvent(String event, Npc npc, Player player)
	{
		switch (event)
		{
			case "ACCEPT":
			{
				if (!canStartQuest(player))
				{
					break;
				}
				
				final QuestState questState = getQuestState(player, true);
				if (!questState.isStarted() && !questState.isCompleted())
				{
					questState.startQuest();
					if (!questState.isCompleted() && player.isInCategory(CategoryType.FOURTH_CLASS_GROUP))
					{
						questState.setCount(getQuestData().getGoal().getCount());
						questState.setCond(QuestCondType.DONE);
						player.sendPacket(new ExQuestNotification(questState));
					}
				}
				break;
			}
			case "TELEPORT":
			{
				QuestState questState = getQuestState(player, false);
				if (questState == null)
				{
					if (!canStartQuest(player))
					{
						break;
					}
					
					questState = getQuestState(player, true);
					
					final NewQuestLocation questLocation = getQuestData().getLocation();
					if (questLocation.getStartLocationId() > 0)
					{
						final Location location = TeleportListData.getInstance().getTeleport(questLocation.getStartLocationId()).getLocation();
						if (teleportToQuestLocation(player, location))
						{
							questState.setCond(QuestCondType.ACT);
							sendAcceptDialog(player);
						}
					}
					break;
				}
				
				final NewQuestLocation questLocation = getQuestData().getLocation();
				if (questState.isCond(QuestCondType.STARTED))
				{
					if (questLocation.getQuestLocationId() > 0)
					{
						final Location location = TeleportListData.getInstance().getTeleport(questLocation.getQuestLocationId()).getLocation();
						if (teleportToQuestLocation(player, location) && (questLocation.getQuestLocationId() == questLocation.getEndLocationId()))
						{
							questState.setCond(QuestCondType.DONE);
							sendEndDialog(player);
						}
					}
				}
				else if (questState.isCond(QuestCondType.DONE) && !questState.isCompleted())
				{
					if (questLocation.getEndLocationId() > 0)
					{
						final Location location = TeleportListData.getInstance().getTeleport(questLocation.getEndLocationId()).getLocation();
						if (teleportToQuestLocation(player, location))
						{
							sendEndDialog(player);
						}
					}
				}
				break;
			}
			case "COMPLETE":
			{
				final QuestState questState = getQuestState(player, false);
				if (questState == null)
				{
					break;
				}
				
				if (questState.isCond(QuestCondType.DONE) && !questState.isCompleted())
				{
					questState.exitQuest(false, true);
					rewardPlayer(player);
				}
				
				final QuestState nextQuestState = player.getQuestState(Q10352_StopTheLizardmen1.class.getSimpleName());
				if (nextQuestState == null)
				{
					player.sendPacket(new ExQuestDialog(10352, QuestDialogType.ACCEPT));
				}
				break;
			}
		}
		
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		final QuestState questState = getQuestState(player, false);
		if ((questState != null) && !questState.isCompleted())
		{
			if ((questState.getCount() == 0) && questState.isCond(QuestCondType.NONE))
			{
				player.sendPacket(new ExQuestDialog(QUEST_ID, QuestDialogType.START));
			}
			else if (questState.isCond(QuestCondType.DONE))
			{
				player.sendPacket(new ExQuestDialog(QUEST_ID, QuestDialogType.END));
			}
		}
		
		npc.showChatWindow(player);
		return null;
	}
	
	@RegisterEvent(EventType.ON_PLAYER_QUEST_ACCEPT)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerQuestAccept(OnPlayerQuestAccept event)
	{
		final Player player = event.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final QuestState questState = getQuestState(player, false);
		if ((questState != null) && !questState.isCompleted())
		{
			questState.setCond(QuestCondType.DONE);
			player.sendPacket(new ExQuestNotification(questState));
		}
	}
	
	@RegisterEvent(EventType.ON_PLAYER_PROFESSION_CHANGE)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerProfessionChange(OnPlayerProfessionChange event)
	{
		final Player player = event.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final QuestState questState = getQuestState(player, false);
		if ((questState != null) && !questState.isCompleted() && player.isInCategory(CategoryType.FOURTH_CLASS_GROUP))
		{
			final int stateCount = questState.getCount();
			if (stateCount != getQuestData().getGoal().getCount())
			{
				questState.setCount(getQuestData().getGoal().getCount());
				questState.setCond(QuestCondType.DONE);
				player.sendPacket(new ExQuestNotification(questState));
			}
		}
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LEVEL_CHANGED)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerLevelChange(OnPlayerLevelChanged event)
	{
		final Player player = event.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final QuestState questState = getQuestState(player, false);
		if ((questState == null) && canStartQuest(player))
		{
			player.sendPacket(new ExQuestDialog(QUEST_ID, QuestDialogType.ACCEPT));
		}
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGIN)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerLogin(OnPlayerLogin event)
	{
		final Player player = event.getPlayer();
		if (player == null)
		{
			return;
		}
		
		final QuestState questState = getQuestState(player, false);
		if ((questState != null) && !questState.isCompleted() && player.isInCategory(CategoryType.FOURTH_CLASS_GROUP))
		{
			final int stateCount = questState.getCount();
			if (stateCount != getQuestData().getGoal().getCount())
			{
				questState.setCount(getQuestData().getGoal().getCount());
				questState.setCond(QuestCondType.DONE);
				player.sendPacket(new ExQuestNotification(questState));
			}
		}
	}
}
