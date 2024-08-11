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
package org.l2jmobius.gameserver.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.network.clientpackets.*;
import org.l2jmobius.gameserver.network.clientpackets.adenadistribution.RequestDivideAdena;
import org.l2jmobius.gameserver.network.clientpackets.adenadistribution.RequestDivideAdenaCancel;
import org.l2jmobius.gameserver.network.clientpackets.adenadistribution.RequestDivideAdenaStart;
import org.l2jmobius.gameserver.network.clientpackets.appearance.RequestExCancelShape_Shifting_Item;
import org.l2jmobius.gameserver.network.clientpackets.appearance.RequestExTryToPutShapeShiftingEnchantSupportItem;
import org.l2jmobius.gameserver.network.clientpackets.appearance.RequestExTryToPutShapeShiftingTargetItem;
import org.l2jmobius.gameserver.network.clientpackets.appearance.RequestShapeShiftingItem;
import org.l2jmobius.gameserver.network.clientpackets.attendance.RequestVipAttendanceCheck;
import org.l2jmobius.gameserver.network.clientpackets.attendance.RequestVipAttendanceItemList;
import org.l2jmobius.gameserver.network.clientpackets.attributechange.RequestChangeAttributeCancel;
import org.l2jmobius.gameserver.network.clientpackets.attributechange.RequestChangeAttributeItem;
import org.l2jmobius.gameserver.network.clientpackets.attributechange.SendChangeAttributeTargetItem;
import org.l2jmobius.gameserver.network.clientpackets.autopeel.ExRequestItemAutoPeel;
import org.l2jmobius.gameserver.network.clientpackets.autopeel.ExRequestReadyItemAutoPeel;
import org.l2jmobius.gameserver.network.clientpackets.autopeel.ExRequestStopItemAutoPeel;
import org.l2jmobius.gameserver.network.clientpackets.autoplay.ExAutoPlaySetting;
import org.l2jmobius.gameserver.network.clientpackets.autoplay.ExRequestActivateAutoShortcut;
import org.l2jmobius.gameserver.network.clientpackets.balok.ExBalrogWarGetReward;
import org.l2jmobius.gameserver.network.clientpackets.balok.ExBalrogWarShowRanking;
import org.l2jmobius.gameserver.network.clientpackets.balok.ExBalrogWarShowUI;
import org.l2jmobius.gameserver.network.clientpackets.balok.ExBalrogWarTeleport;
import org.l2jmobius.gameserver.network.clientpackets.blessing.RequestBlessOptionCancel;
import org.l2jmobius.gameserver.network.clientpackets.blessing.RequestBlessOptionEnchant;
import org.l2jmobius.gameserver.network.clientpackets.blessing.RequestBlessOptionPutItem;
import org.l2jmobius.gameserver.network.clientpackets.captcha.RequestCaptchaAnswer;
import org.l2jmobius.gameserver.network.clientpackets.captcha.RequestRefreshCaptcha;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExCastleWarObserverStart;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExMercenaryCastleWarCastleSiegeAttackerList;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExMercenaryCastleWarCastleSiegeDefenderList;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExMercenaryCastleWarCastleSiegeHudInfo;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExMercenaryCastleWarCastleSiegeInfo;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExPledgeMercenaryMemberJoin;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExPledgeMercenaryMemberList;
import org.l2jmobius.gameserver.network.clientpackets.castlewar.ExPledgeMercenaryRecruitInfoSet;
import org.l2jmobius.gameserver.network.clientpackets.classchange.ExRequestClassChange;
import org.l2jmobius.gameserver.network.clientpackets.classchange.ExRequestClassChangeVerifying;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestCollectionCloseUI;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestCollectionFavoriteList;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestCollectionReceiveReward;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestCollectionRegister;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestCollectionUpdateFavorite;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestExCollectionList;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestExCollectionOpenUI;
import org.l2jmobius.gameserver.network.clientpackets.collection.RequestExCollectionSummary;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionBuyInfo;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionBuyItem;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionCancel;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionDelete;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionInfo;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionList;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionRegister;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionRegisteredItem;
import org.l2jmobius.gameserver.network.clientpackets.commission.RequestCommissionRegistrableItemList;
import org.l2jmobius.gameserver.network.clientpackets.compound.RequestNewEnchantClose;
import org.l2jmobius.gameserver.network.clientpackets.compound.RequestNewEnchantPushOne;
import org.l2jmobius.gameserver.network.clientpackets.compound.RequestNewEnchantPushTwo;
import org.l2jmobius.gameserver.network.clientpackets.compound.RequestNewEnchantRemoveOne;
import org.l2jmobius.gameserver.network.clientpackets.compound.RequestNewEnchantRemoveTwo;
import org.l2jmobius.gameserver.network.clientpackets.compound.RequestNewEnchantRetryToPutItems;
import org.l2jmobius.gameserver.network.clientpackets.compound.RequestNewEnchantTry;
import org.l2jmobius.gameserver.network.clientpackets.crystalization.RequestCrystallizeEstimate;
import org.l2jmobius.gameserver.network.clientpackets.crystalization.RequestCrystallizeItemCancel;
import org.l2jmobius.gameserver.network.clientpackets.dailymission.RequestMissionLevelReceiveReward;
import org.l2jmobius.gameserver.network.clientpackets.dailymission.RequestMissionRewardList;
import org.l2jmobius.gameserver.network.clientpackets.dailymission.RequestOneDayRewardReceive;
import org.l2jmobius.gameserver.network.clientpackets.dailymission.RequestTodoList;
import org.l2jmobius.gameserver.network.clientpackets.dualinventory.RequestExDualInventorySwap;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalInitTalent;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritAbsorb;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritAbsorbInfo;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritEvolution;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritEvolutionInfo;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritExtract;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritExtractInfo;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritInfo;
import org.l2jmobius.gameserver.network.clientpackets.elementalspirits.ExElementalSpiritSetTalent;
import org.l2jmobius.gameserver.network.clientpackets.enchant.RequestExAddEnchantScrollItem;
import org.l2jmobius.gameserver.network.clientpackets.enchant.RequestExCancelEnchantItem;
import org.l2jmobius.gameserver.network.clientpackets.enchant.RequestExRemoveEnchantSupportItem;
import org.l2jmobius.gameserver.network.clientpackets.enchant.RequestExTryToPutEnchantSupportItem;
import org.l2jmobius.gameserver.network.clientpackets.enchant.RequestExTryToPutEnchantTargetItem;
import org.l2jmobius.gameserver.network.clientpackets.enchant.challengepoint.ExRequestResetEnchantChallengePoint;
import org.l2jmobius.gameserver.network.clientpackets.enchant.challengepoint.ExRequestSetEnchantChallengePoint;
import org.l2jmobius.gameserver.network.clientpackets.enchant.multi.ExRequestFinishMultiEnchantScroll;
import org.l2jmobius.gameserver.network.clientpackets.enchant.multi.ExRequestMultiEnchantItemList;
import org.l2jmobius.gameserver.network.clientpackets.enchant.multi.ExRequestSetMultiEnchantItemList;
import org.l2jmobius.gameserver.network.clientpackets.enchant.multi.ExRequestStartMultiEnchantScroll;
import org.l2jmobius.gameserver.network.clientpackets.enchant.multi.ExRequestViewMultiEnchantResult;
import org.l2jmobius.gameserver.network.clientpackets.enchant.single.ExRequestEnchantFailRewardInfo;
import org.l2jmobius.gameserver.network.clientpackets.enchant.single.ExRequestViewEnchantResult;
import org.l2jmobius.gameserver.network.clientpackets.ensoul.RequestItemEnsoul;
import org.l2jmobius.gameserver.network.clientpackets.ensoul.RequestTryEnSoulExtraction;
import org.l2jmobius.gameserver.network.clientpackets.equipmentupgrade.RequestUpgradeSystemResult;
import org.l2jmobius.gameserver.network.clientpackets.equipmentupgradenormal.ExUpgradeSystemNormalRequest;
import org.l2jmobius.gameserver.network.clientpackets.friend.RequestBlockDetailInfo;
import org.l2jmobius.gameserver.network.clientpackets.friend.RequestBlockMemo;
import org.l2jmobius.gameserver.network.clientpackets.friend.RequestFriendDetailInfo;
import org.l2jmobius.gameserver.network.clientpackets.friend.RequestUpdateFriendMemo;
import org.l2jmobius.gameserver.network.clientpackets.gacha.ExUniqueGachaGame;
import org.l2jmobius.gameserver.network.clientpackets.gacha.ExUniqueGachaHistory;
import org.l2jmobius.gameserver.network.clientpackets.gacha.ExUniqueGachaInvenGetItem;
import org.l2jmobius.gameserver.network.clientpackets.gacha.ExUniqueGachaInvenItemList;
import org.l2jmobius.gameserver.network.clientpackets.gacha.ExUniqueGachaOpen;
import org.l2jmobius.gameserver.network.clientpackets.huntingzones.ExTimedHuntingZoneEnter;
import org.l2jmobius.gameserver.network.clientpackets.huntingzones.ExTimedHuntingZoneLeave;
import org.l2jmobius.gameserver.network.clientpackets.huntingzones.ExTimedHuntingZoneList;
import org.l2jmobius.gameserver.network.clientpackets.huntpass.HuntpassSayhasToggle;
import org.l2jmobius.gameserver.network.clientpackets.huntpass.RequestHuntPassBuyPremium;
import org.l2jmobius.gameserver.network.clientpackets.huntpass.RequestHuntPassInfo;
import org.l2jmobius.gameserver.network.clientpackets.huntpass.RequestHuntPassReward;
import org.l2jmobius.gameserver.network.clientpackets.huntpass.RequestHuntPassRewardAll;
import org.l2jmobius.gameserver.network.clientpackets.limitshop.RequestPurchaseLimitShopItemBuy;
import org.l2jmobius.gameserver.network.clientpackets.limitshop.RequestPurchaseLimitShopItemList;
import org.l2jmobius.gameserver.network.clientpackets.luckygame.RequestLuckyGamePlay;
import org.l2jmobius.gameserver.network.clientpackets.luckygame.RequestLuckyGameStartInfo;
import org.l2jmobius.gameserver.network.clientpackets.mablegame.ExRequestMableGameClose;
import org.l2jmobius.gameserver.network.clientpackets.mablegame.ExRequestMableGameOpen;
import org.l2jmobius.gameserver.network.clientpackets.mablegame.ExRequestMableGamePopupOk;
import org.l2jmobius.gameserver.network.clientpackets.mablegame.ExRequestMableGameReset;
import org.l2jmobius.gameserver.network.clientpackets.mablegame.ExRequestMableGameRollDice;
import org.l2jmobius.gameserver.network.clientpackets.magiclamp.ExMagicLampGameInfo;
import org.l2jmobius.gameserver.network.clientpackets.magiclamp.ExMagicLampGameStart;
import org.l2jmobius.gameserver.network.clientpackets.mentoring.ConfirmMenteeAdd;
import org.l2jmobius.gameserver.network.clientpackets.mentoring.RequestMenteeAdd;
import org.l2jmobius.gameserver.network.clientpackets.mentoring.RequestMenteeWaitingList;
import org.l2jmobius.gameserver.network.clientpackets.mentoring.RequestMentorCancel;
import org.l2jmobius.gameserver.network.clientpackets.mentoring.RequestMentorList;
import org.l2jmobius.gameserver.network.clientpackets.newcrest.RequestGetPledgeCrestPreset;
import org.l2jmobius.gameserver.network.clientpackets.newcrest.RequestSetPledgeCrestPreset;
import org.l2jmobius.gameserver.network.clientpackets.newhenna.RequestNewHennaCompose;
import org.l2jmobius.gameserver.network.clientpackets.newhenna.RequestNewHennaEquip;
import org.l2jmobius.gameserver.network.clientpackets.newhenna.RequestNewHennaList;
import org.l2jmobius.gameserver.network.clientpackets.newhenna.RequestNewHennaPotenEnchant;
import org.l2jmobius.gameserver.network.clientpackets.newhenna.RequestNewHennaPotenSelect;
import org.l2jmobius.gameserver.network.clientpackets.newhenna.RequestNewHennaUnequip;
import org.l2jmobius.gameserver.network.clientpackets.newhenna.RequestNewHennaUnequipInfo;
import org.l2jmobius.gameserver.network.clientpackets.newskillenchant.RequestExSkillEnchantCharge;
import org.l2jmobius.gameserver.network.clientpackets.newskillenchant.RequestExSkillEnchantInfo;
import org.l2jmobius.gameserver.network.clientpackets.newskillenchant.RequestExSpExtractInfo;
import org.l2jmobius.gameserver.network.clientpackets.newskillenchant.RequestExSpExtractItem;
import org.l2jmobius.gameserver.network.clientpackets.olympiad.OlympiadMatchMaking;
import org.l2jmobius.gameserver.network.clientpackets.olympiad.OlympiadMatchMakingCancel;
import org.l2jmobius.gameserver.network.clientpackets.olympiad.OlympiadUI;
import org.l2jmobius.gameserver.network.clientpackets.olympiad.RequestExOlympiadMatchListRefresh;
import org.l2jmobius.gameserver.network.clientpackets.olympiad.RequestOlympiadMatchList;
import org.l2jmobius.gameserver.network.clientpackets.olympiad.RequestOlympiadObserverEnd;
import org.l2jmobius.gameserver.network.clientpackets.pet.ExEvolvePet;
import org.l2jmobius.gameserver.network.clientpackets.pet.ExPetEquipItem;
import org.l2jmobius.gameserver.network.clientpackets.pet.ExPetUnequipItem;
import org.l2jmobius.gameserver.network.clientpackets.pet.ExTryPetExtractSystem;
import org.l2jmobius.gameserver.network.clientpackets.pet.RequestExAcquirePetSkill;
import org.l2jmobius.gameserver.network.clientpackets.pledgeV3.RequestExPledgeEnemyDelete;
import org.l2jmobius.gameserver.network.clientpackets.pledgeV3.RequestExPledgeEnemyInfoList;
import org.l2jmobius.gameserver.network.clientpackets.pledgeV3.RequestExPledgeEnemyRegister;
import org.l2jmobius.gameserver.network.clientpackets.pledgeV3.RequestExPledgeV3Info;
import org.l2jmobius.gameserver.network.clientpackets.pledgeV3.RequestExPledgeV3SetAnnounce;
import org.l2jmobius.gameserver.network.clientpackets.pledgebonus.RequestPledgeBonusOpen;
import org.l2jmobius.gameserver.network.clientpackets.pledgebonus.RequestPledgeBonusReward;
import org.l2jmobius.gameserver.network.clientpackets.pledgebonus.RequestPledgeBonusRewardList;
import org.l2jmobius.gameserver.network.clientpackets.pledgedonation.RequestExPledgeContributionList;
import org.l2jmobius.gameserver.network.clientpackets.pledgedonation.RequestExPledgeDonationInfo;
import org.l2jmobius.gameserver.network.clientpackets.pledgedonation.RequestExPledgeDonationRequest;
import org.l2jmobius.gameserver.network.clientpackets.primeshop.RequestBRBuyProduct;
import org.l2jmobius.gameserver.network.clientpackets.primeshop.RequestBRGamePoint;
import org.l2jmobius.gameserver.network.clientpackets.primeshop.RequestBRPresentBuyProduct;
import org.l2jmobius.gameserver.network.clientpackets.primeshop.RequestBRProductInfo;
import org.l2jmobius.gameserver.network.clientpackets.primeshop.RequestBRProductList;
import org.l2jmobius.gameserver.network.clientpackets.primeshop.RequestBRRecentProductList;
import org.l2jmobius.gameserver.network.clientpackets.quest.RequestExQuestAccept;
import org.l2jmobius.gameserver.network.clientpackets.quest.RequestExQuestAcceptableList;
import org.l2jmobius.gameserver.network.clientpackets.quest.RequestExQuestCancel;
import org.l2jmobius.gameserver.network.clientpackets.quest.RequestExQuestComplete;
import org.l2jmobius.gameserver.network.clientpackets.quest.RequestExQuestNotificationAll;
import org.l2jmobius.gameserver.network.clientpackets.quest.RequestExQuestTeleport;
import org.l2jmobius.gameserver.network.clientpackets.quest.RequestExQuestUI;
import org.l2jmobius.gameserver.network.clientpackets.raidbossinfo.RequestRaidBossSpawnInfo;
import org.l2jmobius.gameserver.network.clientpackets.raidbossinfo.RequestRaidServerInfo;
import org.l2jmobius.gameserver.network.clientpackets.randomcraft.ExRequestRandomCraftExtract;
import org.l2jmobius.gameserver.network.clientpackets.randomcraft.ExRequestRandomCraftInfo;
import org.l2jmobius.gameserver.network.clientpackets.randomcraft.ExRequestRandomCraftLockSlot;
import org.l2jmobius.gameserver.network.clientpackets.randomcraft.ExRequestRandomCraftMake;
import org.l2jmobius.gameserver.network.clientpackets.randomcraft.ExRequestRandomCraftRefresh;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestExRankingCharBuffzoneNpcPosition;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestExRankingCharSpawnBuffzoneNpc;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestOlympiadHeroAndLegendInfo;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestOlympiadMyRankingInfo;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestOlympiadRankingInfo;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestPetRankingList;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestPetRankingMyInfo;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestPledgeRankingList;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestPledgeRankingMyInfo;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestPvpRankingList;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestPvpRankingMyInfo;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestRankingCharHistory;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestRankingCharInfo;
import org.l2jmobius.gameserver.network.clientpackets.ranking.RequestRankingCharRankers;
import org.l2jmobius.gameserver.network.clientpackets.revenge.RequestExPvpBookShareRevengeKillerLocation;
import org.l2jmobius.gameserver.network.clientpackets.revenge.RequestExPvpBookShareRevengeList;
import org.l2jmobius.gameserver.network.clientpackets.revenge.RequestExPvpBookShareRevengeReqShareRevengeInfo;
import org.l2jmobius.gameserver.network.clientpackets.revenge.RequestExPvpBookShareRevengeSharedTeleportToKiller;
import org.l2jmobius.gameserver.network.clientpackets.revenge.RequestExPvpBookShareRevengeTeleportToKiller;
import org.l2jmobius.gameserver.network.clientpackets.sayune.RequestFlyMove;
import org.l2jmobius.gameserver.network.clientpackets.sayune.RequestFlyMoveStart;
import org.l2jmobius.gameserver.network.clientpackets.settings.ExInteractModify;
import org.l2jmobius.gameserver.network.clientpackets.settings.ExSaveItemAnnounceSetting;
import org.l2jmobius.gameserver.network.clientpackets.settings.RequestKeyMapping;
import org.l2jmobius.gameserver.network.clientpackets.settings.RequestSaveKeyMapping;
import org.l2jmobius.gameserver.network.clientpackets.shuttle.CannotMoveAnymoreInShuttle;
import org.l2jmobius.gameserver.network.clientpackets.shuttle.MoveToLocationInShuttle;
import org.l2jmobius.gameserver.network.clientpackets.shuttle.RequestShuttleGetOff;
import org.l2jmobius.gameserver.network.clientpackets.shuttle.RequestShuttleGetOn;
import org.l2jmobius.gameserver.network.clientpackets.stats.ExResetStatusBonus;
import org.l2jmobius.gameserver.network.clientpackets.stats.ExSetStatusBonus;
import org.l2jmobius.gameserver.network.clientpackets.steadybox.RequestSteadyBoxLoad;
import org.l2jmobius.gameserver.network.clientpackets.steadybox.RequestSteadyGetReward;
import org.l2jmobius.gameserver.network.clientpackets.steadybox.RequestSteadyOpenBox;
import org.l2jmobius.gameserver.network.clientpackets.steadybox.RequestSteadyOpenSlot;
import org.l2jmobius.gameserver.network.clientpackets.storereview.ExRequestPrivateStoreSearchList;
import org.l2jmobius.gameserver.network.clientpackets.storereview.ExRequestPrivateStoreSearchStatistics;
import org.l2jmobius.gameserver.network.clientpackets.subjugation.RequestSubjugationGacha;
import org.l2jmobius.gameserver.network.clientpackets.subjugation.RequestSubjugationGachaUI;
import org.l2jmobius.gameserver.network.clientpackets.subjugation.RequestSubjugationList;
import org.l2jmobius.gameserver.network.clientpackets.subjugation.RequestSubjugationRanking;
import org.l2jmobius.gameserver.network.clientpackets.surveillance.ExRequestUserWatcherAdd;
import org.l2jmobius.gameserver.network.clientpackets.surveillance.ExRequestUserWatcherDelete;
import org.l2jmobius.gameserver.network.clientpackets.surveillance.ExRequestUserWatcherTargetList;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExRequestSharedLocationTeleport;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExRequestSharedLocationTeleportUi;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExRequestSharingLocationUi;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExRequestTeleport;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExRequestTeleportFavoriteList;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExRequestTeleportFavoritesAddDel;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExRequestTeleportFavoritesUIToggle;
import org.l2jmobius.gameserver.network.clientpackets.teleports.ExTeleportToRaidPosition;
import org.l2jmobius.gameserver.network.clientpackets.teleports.RequestRaidTeleportInfo;
import org.l2jmobius.gameserver.network.clientpackets.training.NotifyTrainingRoomEnd;
import org.l2jmobius.gameserver.network.clientpackets.variation.ExApplyVariationOption;
import org.l2jmobius.gameserver.network.clientpackets.variation.ExVariationCloseUi;
import org.l2jmobius.gameserver.network.clientpackets.variation.ExVariationOpenUi;
import org.l2jmobius.gameserver.network.clientpackets.variation.RequestConfirmGemStone;
import org.l2jmobius.gameserver.network.clientpackets.variation.RequestRefine;
import org.l2jmobius.gameserver.network.clientpackets.vip.ExRequestVipInfo;
import org.l2jmobius.gameserver.network.clientpackets.vip.RequestVipLuckGameInfo;
import org.l2jmobius.gameserver.network.clientpackets.worldexchange.ExWorldExchangeAveragePrice;
import org.l2jmobius.gameserver.network.clientpackets.worldexchange.ExWorldExchangeBuyItem;
import org.l2jmobius.gameserver.network.clientpackets.worldexchange.ExWorldExchangeItemList;
import org.l2jmobius.gameserver.network.clientpackets.worldexchange.ExWorldExchangeRegisterItem;
import org.l2jmobius.gameserver.network.clientpackets.worldexchange.ExWorldExchangeSettleList;
import org.l2jmobius.gameserver.network.clientpackets.worldexchange.ExWorldExchangeSettleRecvResult;
import org.l2jmobius.gameserver.network.clientpackets.worldexchange.ExWorldExchangeTotalList;

/**
 * @author Mobius
 */
public enum ExClientPackets
{
	REQUEST_GOTO_LOBBY(0x33, RequestGotoLobby::new, ConnectionState.AUTHENTICATED),
	REQUEST_EX_2ND_PASSWORD_CHECK(0xA6, RequestEx2ndPasswordCheck::new, ConnectionState.AUTHENTICATED),
	REQUEST_EX_2ND_PASSWORD_VERIFY(0xA7, RequestEx2ndPasswordVerify::new, ConnectionState.AUTHENTICATED),
	REQUEST_EX_2ND_PASSWORD_REQ(0xA8, RequestEx2ndPasswordReq::new, ConnectionState.AUTHENTICATED),
	REQUEST_CHARACTER_NAME_CREATABLE(0xA9, RequestCharacterNameCreatable::new, ConnectionState.AUTHENTICATED),
	REQUEST_MANOR_LIST(0x01, RequestManorList::new, ConnectionState.IN_GAME),
	REQUEST_PROCEDURE_CROP_LIST(0x02, RequestProcureCropList::new, ConnectionState.IN_GAME),
	REQUEST_SET_SEED(0x03, RequestSetSeed::new, ConnectionState.IN_GAME),
	REQUEST_SET_CROP(0x04, RequestSetCrop::new, ConnectionState.IN_GAME),
	REQUEST_WRITE_HERO_WORDS(0x05, RequestWriteHeroWords::new, ConnectionState.IN_GAME),
	REQUEST_EX_ASK_JOIN_MPCC(0x06, RequestExAskJoinMPCC::new, ConnectionState.IN_GAME),
	REQUEST_EX_ACCEPT_JOIN_MPCC(0x07, RequestExAcceptJoinMPCC::new, ConnectionState.IN_GAME),
	REQUEST_EX_OUST_FROM_MPCC(0x08, RequestExOustFromMPCC::new, ConnectionState.IN_GAME),
	REQUEST_OUST_FROM_PARTY_ROOM(0x09, RequestOustFromPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_DISMISS_PARTY_ROOM(0x0A, RequestDismissPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_WITHDRAW_PARTY_ROOM(0x0B, RequestWithdrawPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_CHANGE_PARTY_LEADER(0x0C, RequestChangePartyLeader::new, ConnectionState.IN_GAME),
	REQUEST_AUTO_SOULSHOT(0x0D, RequestAutoSoulShot::new, ConnectionState.IN_GAME),
	REQUEST_EX_ENCHANT_SKILL_INFO(0x0E, RequestExEnchantSkillInfo::new, ConnectionState.IN_GAME),
	REQUEST_EX_ENCHANT_SKILL(0x0F, RequestExEnchantSkill::new, ConnectionState.IN_GAME),
	REQUEST_EX_PLEDGE_CREST_LARGE(0x10, RequestExPledgeCrestLarge::new, ConnectionState.IN_GAME),
	REQUEST_EX_SET_PLEDGE_CREST_LARGE(0x11, RequestExSetPledgeCrestLarge::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_SET_ACADEMY_MASTER(0x12, RequestPledgeSetAcademyMaster::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_POWER_GRADE_LIST(0x13, RequestPledgePowerGradeList::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_MEMBER_POWER_INFO(0x14, RequestPledgeMemberPowerInfo::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_SET_MEMBER_POWER_GRADE(0x15, RequestPledgeSetMemberPowerGrade::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_MEMBER_INFO(0x16, RequestPledgeMemberInfo::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_WAR_LIST(0x17, RequestPledgeWarList::new, ConnectionState.IN_GAME),
	REQUEST_EX_FISH_RANKING(0x18, RequestExFishRanking::new, ConnectionState.IN_GAME),
	REQUEST_PCCAFE_COUPON_USE(0x19, RequestPCCafeCouponUse::new, ConnectionState.IN_GAME),
	REQUEST_SERVER_LOGIN(0x1A, null, ConnectionState.IN_GAME),
	REQUEST_DUEL_START(0x1B, RequestDuelStart::new, ConnectionState.IN_GAME),
	REQUEST_DUAL_ANSWER_START(0x1C, RequestDuelAnswerStart::new, ConnectionState.IN_GAME),
	REQUEST_EX_SET_TUTORIAL(0x1D, null, ConnectionState.IN_GAME),
	REQUEST_EX_RQ_ITEM_LINK(0x1E, RequestExRqItemLink::new, ConnectionState.IN_GAME),
	CANNOT_MOVE_ANYMORE_AIR_SHIP(0x1F, null, ConnectionState.IN_GAME),
	MOVE_TO_LOCATION_IN_AIR_SHIP(0x20, MoveToLocationInAirShip::new, ConnectionState.IN_GAME),
	REQUEST_KEY_MAPPING(0x21, RequestKeyMapping::new, ConnectionState.ENTERING, ConnectionState.IN_GAME),
	REQUEST_SAVE_KEY_MAPPING(0x22, RequestSaveKeyMapping::new, ConnectionState.IN_GAME),
	REQUEST_EX_REMOVE_ITEM_ATTRIBUTE(0x23, RequestExRemoveItemAttribute::new, ConnectionState.IN_GAME),
	REQUEST_SAVE_INVENTORY_ORDER(0x24, RequestSaveInventoryOrder::new, ConnectionState.IN_GAME),
	REQUEST_EXIT_PARTY_MATCHING_WAITING_ROOM(0x25, RequestExitPartyMatchingWaitingRoom::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_TARGET_ITEM(0x26, RequestConfirmTargetItem::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_REFINER_ITEM(0x27, RequestConfirmRefinerItem::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_GEMSTONE(0x28, RequestConfirmGemStone::new, ConnectionState.IN_GAME),
	REQUEST_OLYMPIAD_OBSERVER_END(0x29, RequestOlympiadObserverEnd::new, ConnectionState.IN_GAME),
	REQUEST_CURSED_WEAPON_LIST(0x2A, RequestCursedWeaponList::new, ConnectionState.IN_GAME),
	REQUEST_CURSED_WEAPON_LOCATION(0x2B, RequestCursedWeaponLocation::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_REORGANIZE_MEMBER(0x2C, RequestPledgeReorganizeMember::new, ConnectionState.IN_GAME),
	REQUEST_EX_MPCC_SHOW_PARTY_MEMBERS_INFO(0x2D, RequestExMPCCShowPartyMembersInfo::new, ConnectionState.IN_GAME),
	REQUEST_OLYMPIAD_MATCH_LIST(0x2E, RequestOlympiadMatchList::new, ConnectionState.IN_GAME),
	REQUEST_ASK_JOIN_PARTY_ROOM(0x2F, RequestAskJoinPartyRoom::new, ConnectionState.IN_GAME),
	ANSWER_JOIN_PARTY_ROOM(0x30, AnswerJoinPartyRoom::new, ConnectionState.IN_GAME),
	REQUEST_LIST_PARTY_MATCHING_WAITING_ROOM(0x31, RequestListPartyMatchingWaitingRoom::new, ConnectionState.IN_GAME),
	REQUEST_EX_ENCHANT_ITEM_ATTRIBUTE(0x32, RequestExEnchantItemAttribute::new, ConnectionState.IN_GAME),
	CANNOT_AIRSHIP_MOVE_ANYMORE(0x34, null, ConnectionState.IN_GAME),
	MOVE_TO_LOCATION_AIR_SHIP(0x35, MoveToLocationAirShip::new, ConnectionState.IN_GAME),
	REQUEST_BID_ITEM_AUCTION(0x36, RequestBidItemAuction::new, ConnectionState.IN_GAME),
	REQUEST_INFO_ITEM_AUCTION(0x37, RequestInfoItemAuction::new, ConnectionState.IN_GAME),
	REQUEST_EX_CHANGE_NAME(0x38, RequestExChangeName::new, ConnectionState.IN_GAME),
	REQUEST_ALL_CASTLE_INFO(0x39, RequestAllCastleInfo::new, ConnectionState.IN_GAME),
	REQUEST_ALL_FORTRESS_INFO(0x3A, RequestAllFortressInfo::new, ConnectionState.IN_GAME),
	REQUEST_ALL_AGIT_INGO(0x3B, RequestAllAgitInfo::new, ConnectionState.IN_GAME),
	REQUEST_FORTRESS_SIEGE_INFO(0x3C, RequestFortressSiegeInfo::new, ConnectionState.IN_GAME),
	REQUEST_GET_BOSS_RECORD(0x3D, RequestGetBossRecord::new, ConnectionState.IN_GAME),
	REQUEST_REFINE(0x3E, RequestRefine::new, ConnectionState.IN_GAME),
	REQUEST_CONFIRM_CANCEL_ITEM(0x3F, RequestConfirmCancelItem::new, ConnectionState.IN_GAME),
	REQUEST_REFINE_CANCEL(0x40, RequestRefineCancel::new, ConnectionState.IN_GAME),
	REQUEST_EX_MAGIC_SKILL_USE_GROUND(0x41, RequestExMagicSkillUseGround::new, ConnectionState.IN_GAME),
	REQUEST_DUEL_SURRENDER(0x42, RequestDuelSurrender::new, ConnectionState.IN_GAME),
	REQUEST_EX_ENCHANT_SKILL_INFO_DETAIL(0x43, RequestExEnchantSkillInfoDetail::new, ConnectionState.IN_GAME),
	REQUEST_ANTI_FREE_SERVER(0x44, null, ConnectionState.IN_GAME),
	REQUEST_FORTRESS_MAP_INFO(0x45, RequestFortressMapInfo::new, ConnectionState.IN_GAME),
	REQUEST_PVP_MATCH_RECORD(0x46, RequestPVPMatchRecord::new, ConnectionState.IN_GAME),
	SET_PRIVATE_STORE_WHOLE_MSG(0x47, SetPrivateStoreWholeMsg::new, ConnectionState.IN_GAME),
	REQUEST_DISPEL(0x48, RequestDispel::new, ConnectionState.IN_GAME),
	REQUEST_EX_TRY_TO_PUT_ENCHANT_TARGET_ITEM(0x49, RequestExTryToPutEnchantTargetItem::new, ConnectionState.IN_GAME),
	REQUEST_EX_TRY_TO_PUT_ENCHANT_SUPPORT_ITEM(0x4A, RequestExTryToPutEnchantSupportItem::new, ConnectionState.IN_GAME),
	REQUEST_EX_CANCEL_ENCHANT_ITEM(0x4B, RequestExCancelEnchantItem::new, ConnectionState.IN_GAME),
	REQUEST_CHANGE_NICKNAME_COLOR(0x4C, RequestChangeNicknameColor::new, ConnectionState.IN_GAME),
	REQUEST_RESET_NICKNAME(0x4D, RequestResetNickname::new, ConnectionState.IN_GAME),
	EX_BOOKMARK_PACKET(0x4E, null, ConnectionState.IN_GAME),
	REQUEST_WITHDRAW_PREMIUM_ITEM(0x4F, RequestWithDrawPremiumItem::new, ConnectionState.IN_GAME),
	REQUEST_EX_JUMP(0x50, null, ConnectionState.IN_GAME),
	REQUEST_EX_START_SHOW_CRATAE_CUBE_RANK(0x51, null, ConnectionState.IN_GAME),
	REQUEST_EX_STOP_SHOW_CRATAE_CUBE_RANK(0x52, null, ConnectionState.IN_GAME),
	NOTIFY_START_MINI_GAME(0x53, null, ConnectionState.IN_GAME),
	REQUEST_EX_JOIN_DOMINION_WAR(0x54, null, ConnectionState.IN_GAME),
	REQUEST_EX_DOMINION_INFO(0x55, null, ConnectionState.IN_GAME),
	REQUEST_EX_CLEFT_ENTER(0x56, null, ConnectionState.IN_GAME),
	REQUEST_EX_CUBE_GAME_CHANGE_TEAM(0x57, null, ConnectionState.IN_GAME),
	END_SCENE_PLAYER(0x58, EndScenePlayer::new, ConnectionState.IN_GAME),
	REQUEST_EX_CUBE_GAME_READY_ANSWER(0x59, null, ConnectionState.IN_GAME),
	REQUEST_EX_LIST_MPCC_WAITING(0x5A, RequestExListMpccWaiting::new, ConnectionState.IN_GAME),
	REQUEST_EX_MANAGE_MPCC_ROOM(0x5B, RequestExManageMpccRoom::new, ConnectionState.IN_GAME),
	REQUEST_EX_JOIN_MPCC_ROOM(0x5C, RequestExJoinMpccRoom::new, ConnectionState.IN_GAME),
	REQUEST_EX_OUST_FROM_MPCC_ROOM(0x5D, RequestExOustFromMpccRoom::new, ConnectionState.IN_GAME),
	REQUEST_EX_DISMISS_MPCC_ROOM(0x5E, RequestExDismissMpccRoom::new, ConnectionState.IN_GAME),
	REQUEST_EX_WITHDRAW_MPCC_ROOM(0x5F, RequestExWithdrawMpccRoom::new, ConnectionState.IN_GAME),
	REQUEST_SEED_PHASE(0x60, RequestSeedPhase::new, ConnectionState.IN_GAME),
	REQUEST_EX_MPCC_PARTYMASTER_LIST(0x61, RequestExMpccPartymasterList::new, ConnectionState.IN_GAME),
	REQUEST_POST_ITEM_LIST(0x62, RequestPostItemList::new, ConnectionState.IN_GAME),
	REQUEST_SEND_POST(0x63, RequestSendPost::new, ConnectionState.IN_GAME),
	REQUEST_RECEIVED_POST_LIST(0x64, RequestReceivedPostList::new, ConnectionState.IN_GAME),
	REQUEST_DELETE_RECEIVED_POST(0x65, RequestDeleteReceivedPost::new, ConnectionState.IN_GAME),
	REQUEST_RECEIVED_POST(0x66, RequestReceivedPost::new, ConnectionState.IN_GAME),
	REQUEST_POST_ATTACHMENT(0x67, RequestPostAttachment::new, ConnectionState.IN_GAME),
	REQUEST_REJECT_POST_ATTACHMENT(0x68, RequestRejectPostAttachment::new, ConnectionState.IN_GAME),
	REQUEST_SENT_POST_LIST(0x69, RequestSentPostList::new, ConnectionState.IN_GAME),
	REQUEST_DELETE_SENT_POST(0x6A, RequestDeleteSentPost::new, ConnectionState.IN_GAME),
	REQUEST_SENT_POST(0x6B, RequestSentPost::new, ConnectionState.IN_GAME),
	REQUEST_CANCEL_POST_ATTACHMENT(0x6C, RequestCancelPostAttachment::new, ConnectionState.IN_GAME),
	REQUEST_SHOW_NEW_USER_PETITION(0x6D, null, ConnectionState.IN_GAME),
	REQUEST_SHOW_STEP_TWO(0x6E, null, ConnectionState.IN_GAME),
	REQUEST_SHOW_STEP_THREE(0x6F, null, ConnectionState.IN_GAME),
	EX_CONNECT_TO_RAID_SERVER(0x70, null, ConnectionState.IN_GAME),
	EX_RETURN_FROM_RAID_SERVER(0x71, null, ConnectionState.IN_GAME),
	REQUEST_REFUND_ITEM(0x72, RequestRefundItem::new, ConnectionState.IN_GAME),
	REQUEST_BUI_SELL_UI_CLOSE(0x73, RequestBuySellUIClose::new, ConnectionState.IN_GAME),
	REQUEST_EX_EVENT_MATCH_OBSERVER_END(0x74, null, ConnectionState.IN_GAME),
	REQUEST_PARTY_LOOT_MODIFICATION(0x75, RequestPartyLootModification::new, ConnectionState.IN_GAME),
	ANSWER_PARTY_LOOT_MODIFICATION(0x76, AnswerPartyLootModification::new, ConnectionState.IN_GAME),
	ANSWER_COUPLE_ACTION(0x77, AnswerCoupleAction::new, ConnectionState.IN_GAME),
	BR_EVENT_RANKER_LIST(0x78, BrEventRankerList::new, ConnectionState.IN_GAME),
	REQUEST_ASK_MEMBER_SHIP(0x79, null, ConnectionState.IN_GAME),
	REQUEST_ADD_EXPAND_QUEST_ALARM(0x7A, RequestAddExpandQuestAlarm::new, ConnectionState.IN_GAME),
	REQUEST_VOTE_NEW(0x7B, RequestVoteNew::new, ConnectionState.IN_GAME),
	REQUEST_SHUTTLE_GET_ON(0x7C, RequestShuttleGetOn::new, ConnectionState.IN_GAME),
	REQUEST_SHUTTLE_GET_OFF(0x7D, RequestShuttleGetOff::new, ConnectionState.IN_GAME),
	MOVE_TO_LOCATION_IN_SHUTTLE(0x7E, MoveToLocationInShuttle::new, ConnectionState.IN_GAME),
	CANNOT_MOVE_ANYMORE_IN_SHUTTLE(0x7F, CannotMoveAnymoreInShuttle::new, ConnectionState.IN_GAME),
	REQUEST_AGIT_ACTION(0x80, null, ConnectionState.IN_GAME), // TODO: Implement / HANDLE SWITCH
	REQUEST_EX_ADD_CONTACT_TO_CONTACT_LIST(0x81, RequestExAddContactToContactList::new, ConnectionState.IN_GAME),
	REQUEST_EX_DELETE_CONTACT_FROM_CONTACT_LIST(0x82, RequestExDeleteContactFromContactList::new, ConnectionState.IN_GAME),
	REQUEST_EX_SHOW_CONTACT_LIST(0x83, RequestExShowContactList::new, ConnectionState.IN_GAME),
	REQUEST_EX_FRIEND_LIST_EXTENDED(0x84, RequestExFriendListExtended::new, ConnectionState.IN_GAME),
	REQUEST_EX_OLYMPIAD_MATCH_LIST_REFRESH(0x85, RequestExOlympiadMatchListRefresh::new, ConnectionState.IN_GAME),
	REQUEST_BR_GAME_POINT(0x86, RequestBRGamePoint::new, ConnectionState.IN_GAME),
	REQUEST_BR_PRODUCT_LIST(0x87, RequestBRProductList::new, ConnectionState.IN_GAME),
	REQUEST_BR_PRODUCT_INFO(0x88, RequestBRProductInfo::new, ConnectionState.IN_GAME),
	REQUEST_BR_BUI_PRODUCT(0x89, RequestBRBuyProduct::new, ConnectionState.IN_GAME),
	REQUEST_BR_RECENT_PRODUCT_LIST(0x8A, RequestBRRecentProductList::new, ConnectionState.IN_GAME),
	REQUEST_BR_MINI_GAME_LOAD_SCORES(0x8B, null, ConnectionState.IN_GAME),
	REQUEST_BR_MINI_GAME_INSERT_SCORE(0x8C, null, ConnectionState.IN_GAME),
	REQUEST_EX_BR_LECTURE_MARK(0x8D, null, ConnectionState.IN_GAME),
	REQUEST_CRYSTALLIZE_ESTIMATE(0x8E, RequestCrystallizeEstimate::new, ConnectionState.IN_GAME),
	REQUEST_CRYSTALLIZE_ITEM_CANCEL(0x8F, RequestCrystallizeItemCancel::new, ConnectionState.IN_GAME),
	REQUEST_SCENE_EX_ESCAPE_SCENE(0x90, RequestExEscapeScene::new, ConnectionState.IN_GAME),
	REQUEST_FLY_MOVE(0x91, RequestFlyMove::new, ConnectionState.IN_GAME),
	REQUEST_SURRENDER_PLEDGE_WAR_EX(0x92, null, ConnectionState.IN_GAME),
	REQUEST_DYNAMIC_QUEST_ACTION(0x93, null, ConnectionState.IN_GAME), // TODO: Implement / HANDLE SWITCH
	REQUEST_FRIEND_DETAIL_INFO(0x94, RequestFriendDetailInfo::new, ConnectionState.IN_GAME),
	REQUEST_UPDATE_FRIEND_MEMO(0x95, RequestUpdateFriendMemo::new, ConnectionState.IN_GAME),
	REQUEST_UPDATE_BLOCK_MEMO(0x96, RequestBlockMemo::new, ConnectionState.IN_GAME),
	REQUEST_INZONE_PARTY_INFO_HISTORY(0x97, null, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_REGISTRABLE_ITEM_LIST(0x98, RequestCommissionRegistrableItemList::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_INFO(0x99, RequestCommissionInfo::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_REGISTER(0x9A, RequestCommissionRegister::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_CANCEL(0x9B, RequestCommissionCancel::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_DELETE(0x9C, RequestCommissionDelete::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_LIST(0x9D, RequestCommissionList::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_BUY_INFO(0x9E, RequestCommissionBuyInfo::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_BUY_ITEM(0x9F, RequestCommissionBuyItem::new, ConnectionState.IN_GAME),
	REQUEST_COMMISSION_REGISTERED_ITEM(0xA0, RequestCommissionRegisteredItem::new, ConnectionState.IN_GAME),
	REQUEST_CALL_TO_CHANGE_CLASS(0xA1, null, ConnectionState.IN_GAME),
	REQUEST_CHANGE_TO_AWAKENED_CLASS(0xA2, RequestChangeToAwakenedClass::new, ConnectionState.IN_GAME),
	REQUEST_WORLD_STATISTICS(0xA3, null, ConnectionState.IN_GAME),
	REQUEST_USER_STATISTICS(0xA4, null, ConnectionState.IN_GAME),
	REQUEST_24HZ_SESSION_ID(0xA5, null, ConnectionState.IN_GAME),
	REQUEST_GOODS_INVENTORY_INFO(0xAA, null, ConnectionState.IN_GAME),
	REQUEST_GOODS_INVENTORY_ITEM(0xAB, null, ConnectionState.IN_GAME),
	REQUEST_FIRST_PLAY_START(0xAC, null, ConnectionState.IN_GAME),
	REQUEST_FLY_MOVE_START(0xAD, RequestFlyMoveStart::new, ConnectionState.IN_GAME),
	REQUEST_HARDWARE_INFO(0xAE, RequestHardWareInfo::new, ConnectionState.values()),
	USER_INTERFACE_INFO(0xAF, null, ConnectionState.IN_GAME),
	SEND_CHANGE_ATTRIBUTE_TARGET_ITEM(0xB0, SendChangeAttributeTargetItem::new, ConnectionState.IN_GAME),
	REQUEST_CHANGE_ATTRIBUTE_ITEM(0xB1, RequestChangeAttributeItem::new, ConnectionState.IN_GAME),
	REQUEST_CHANGE_ATTRIBUTE_CANCEL(0xB2, RequestChangeAttributeCancel::new, ConnectionState.IN_GAME),
	REQUEST_BR_PRESENT_BUY_PRODUCT(0xB3, RequestBRPresentBuyProduct::new, ConnectionState.IN_GAME),
	CONFIRM_MENTEE_ADD(0xB4, ConfirmMenteeAdd::new, ConnectionState.IN_GAME),
	REQUEST_MENTOR_CANCEL(0xB5, RequestMentorCancel::new, ConnectionState.IN_GAME),
	REQUEST_MENTOR_LIST(0xB6, RequestMentorList::new, ConnectionState.IN_GAME),
	REQUEST_MENTEE_ADD(0xB7, RequestMenteeAdd::new, ConnectionState.IN_GAME),
	REQUEST_MENTEE_WAITING_LIST(0xB8, RequestMenteeWaitingList::new, ConnectionState.IN_GAME),
	REQUEST_CLAN_ASK_JOIN_BY_NAME(0xB9, RequestClanAskJoinByName::new, ConnectionState.IN_GAME),
	REQUEST_IN_ZONE_WAITING_TIME(0xBA, RequestInzoneWaitingTime::new, ConnectionState.IN_GAME),
	REQUEST_JOIN_CURIOUS_HOUSE(0xBB, null, ConnectionState.IN_GAME),
	REQUEST_CANCEL_CURIOUS_HOUSE(0xBC, null, ConnectionState.IN_GAME),
	REQUEST_LEAVE_CURIOUS_HOUSE(0xBD, null, ConnectionState.IN_GAME),
	REQUEST_OBSERVING_LIST_CURIOUS_HOUSE(0xBE, null, ConnectionState.IN_GAME),
	REQUEST_OBSERVING_CURIOUS_HOUSE(0xBF, null, ConnectionState.IN_GAME),
	REQUEST_LEAVE_OBSERVING_CURIOUS_HOUSE(0xC0, null, ConnectionState.IN_GAME),
	REQUEST_CURIOUS_HOUSE_HTML(0xC1, null, ConnectionState.IN_GAME),
	REQUEST_CURIOUS_HOUSE_RECORD(0xC2, null, ConnectionState.IN_GAME),
	EX_SYSSTRING(0xC3, null, ConnectionState.IN_GAME),
	REQUEST_EX_TRY_TO_PUT_SHAPE_SHIFTING_TARGET_ITEM(0xC4, RequestExTryToPutShapeShiftingTargetItem::new, ConnectionState.IN_GAME),
	REQUEST_EX_TRY_TO_PUT_SHAPE_SHIFTING_ENCHANT_SUPPORT_ITEM(0xC5, RequestExTryToPutShapeShiftingEnchantSupportItem::new, ConnectionState.IN_GAME),
	REQUEST_EX_CANCEL_SHAPE_SHIFTING_ITEM(0xC6, RequestExCancelShape_Shifting_Item::new, ConnectionState.IN_GAME),
	REQUEST_SHAPE_SHIFTING_ITEM(0xC7, RequestShapeShiftingItem::new, ConnectionState.IN_GAME),
	NC_GUARD_SEND_DATA_TO_SERVER(0xC8, null, ConnectionState.IN_GAME),
	REQUEST_EVENT_KALIE_TOKEN(0xC9, null, ConnectionState.IN_GAME),
	REQUEST_SHOW_BEAUTY_LIST(0xCA, RequestShowBeautyList::new, ConnectionState.IN_GAME),
	REQUEST_REGIST_BEAUTY(0xCB, RequestRegistBeauty::new, ConnectionState.IN_GAME),
	REQUEST_SHOW_RESET_BEAUTY(0xCC, null, ConnectionState.IN_GAME),
	REQUEST_SHOW_RESET_SHOP_LIST(0xCD, RequestShowResetShopList::new, ConnectionState.IN_GAME),
	NET_PING(0xCE, null, ConnectionState.IN_GAME),
	REQUEST_BR_ADD_BASKET_PRODUCT_INFO(0xCF, null, ConnectionState.IN_GAME),
	REQUEST_BR_DELETE_BASKET_PRODUCT_INFO(0xD0, null, ConnectionState.IN_GAME),
	REQUEST_BR_EXIST_NEW_PRODUCT(0xD1, null, ConnectionState.IN_GAME),
	REQUEST_EX_EVENT_CAMPAIGN_INFO(0xD2, null, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_RECRUIT_INFO(0xD3, RequestPledgeRecruitInfo::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_RECRUIT_BOARD_SEARCH(0xD4, RequestPledgeRecruitBoardSearch::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_RECRUIT_BOARD_ACCESS(0xD5, RequestPledgeRecruitBoardAccess::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_RECRUIT_BOARD_DETAIL(0xD6, RequestPledgeRecruitBoardDetail::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_WAITING_APPLY(0xD7, RequestPledgeWaitingApply::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_WAITING_APPLIED(0xD8, RequestPledgeWaitingApplied::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_WAITING_LIST(0xD9, RequestPledgeWaitingList::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_WAITING_USER(0xDA, RequestPledgeWaitingUser::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_WAITING_USER_ACCEPT(0xDB, RequestPledgeWaitingUserAccept::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_DRAFT_LIST_SEARCH(0xDC, RequestPledgeDraftListSearch::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_DRAFT_LIST_APPLY(0xDD, RequestPledgeDraftListApply::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_RECRUIT_APPLY_INFO(0xDE, RequestPledgeRecruitApplyInfo::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_JOIN_SYS(0xDF, null, ConnectionState.IN_GAME),
	RESPONSE_PETITION_ALARM(0xE0, null, ConnectionState.IN_GAME),
	NOTIFY_EXIT_BEAUTY_SHOP(0xE1, NotifyExitBeautyShop::new, ConnectionState.IN_GAME),
	REQUEST_REGISTER_XMAS_WISH_CARD(0xE2, null, ConnectionState.IN_GAME),
	REQUEST_EX_ADD_ENCHANT_SCROLL_ITEM(0xE3, RequestExAddEnchantScrollItem::new, ConnectionState.IN_GAME),
	REQUEST_EX_REMOVE_ENCHANT_SUPPORT_ITEM(0xE4, RequestExRemoveEnchantSupportItem::new, ConnectionState.IN_GAME),
	REQUEST_CARD_REWARD(0xE5, null, ConnectionState.IN_GAME),
	REQUEST_DIVIDE_ADENA_START(0xE6, RequestDivideAdenaStart::new, ConnectionState.IN_GAME),
	REQUEST_DIVIDE_ADENA_CANCEL(0xE7, RequestDivideAdenaCancel::new, ConnectionState.IN_GAME),
	REQUEST_DIVIDE_ADENA(0xE8, RequestDivideAdena::new, ConnectionState.IN_GAME),
	REQUEST_ACQUIRE_ABILITY_LIST(0xE9, null, ConnectionState.IN_GAME),
	REQUEST_ABILITY_LIST(0xEA, null, ConnectionState.IN_GAME),
	REQUEST_RESET_ABILITY_POINT(0xEB, null, ConnectionState.IN_GAME),
	REQUEST_CHANGE_ABILITY_POINT(0xEC, null, ConnectionState.IN_GAME),
	REQUEST_STOP_MOVE(0xED, RequestStopMove::new, ConnectionState.IN_GAME),
	REQUEST_ABILITY_WND_OPEN(0xEE, null, ConnectionState.IN_GAME),
	REQUEST_ABILITY_WND_CLOSE(0xEF, null, ConnectionState.IN_GAME),
	REQUEST_LUCKY_GAME_START_INFO(0xF0, RequestLuckyGameStartInfo::new, ConnectionState.IN_GAME),
	REQUEST_LUCKY_GAME_PLAY(0xF1, RequestLuckyGamePlay::new, ConnectionState.IN_GAME),
	NOTIFY_TRAINING_ROOM_END(0xF2, NotifyTrainingRoomEnd::new, ConnectionState.IN_GAME),
	REQUEST_NEW_ENCHANT_PUSH_ONE(0xF3, RequestNewEnchantPushOne::new, ConnectionState.IN_GAME),
	REQUEST_NEW_ENCHANT_REMOVE_ONE(0xF4, RequestNewEnchantRemoveOne::new, ConnectionState.IN_GAME),
	REQUEST_NEW_ENCHANT_PUSH_TWO(0xF5, RequestNewEnchantPushTwo::new, ConnectionState.IN_GAME),
	REQUEST_NEW_ENCHANT_REMOVE_TWO(0xF6, RequestNewEnchantRemoveTwo::new, ConnectionState.IN_GAME),
	REQUEST_NEW_ENCHANT_CLOSE(0xF7, RequestNewEnchantClose::new, ConnectionState.IN_GAME),
	REQUEST_NEW_ENCHANT_TRY(0xF8, RequestNewEnchantTry::new, ConnectionState.IN_GAME),
	REQUEST_NEW_ENCHANT_RETRY_TO_PUT_ITEMS(0xF9, RequestNewEnchantRetryToPutItems::new, ConnectionState.IN_GAME),
	EX_REQUEST_CARD_REWARD_LIST(0xFA, null, ConnectionState.IN_GAME),
	EX_REQUEST_ACCOUNT_ATTENDANCE_INFO(0xFB, null, ConnectionState.IN_GAME),
	EX_REQUEST_ACCOUNT_ATTENDANCE_REWARD(0xFC, null, ConnectionState.IN_GAME),
	REQUEST_TARGET_ACTION_MENU(0xFD, RequestTargetActionMenu::new, ConnectionState.IN_GAME),
	EX_SEND_SELECTED_QUEST_ZONE_ID(0xFE, ExSendSelectedQuestZoneID::new, ConnectionState.IN_GAME),
	REQUEST_ALCHEMY_SKILL_LIST(0xFF, RequestAlchemySkillList::new, ConnectionState.IN_GAME),
	REQUEST_ALCHEMY_TRY_MIX_CUBE(0x100, null, ConnectionState.IN_GAME),
	REQUEST_ALCHEMY_CONVERSION(0x101, null, ConnectionState.IN_GAME),
	SEND_EXECUTED_UI_EVENTS_COUNT(0x102, null, ConnectionState.IN_GAME),
	EX_SEND_CLIENT_INI(0x103, null, ConnectionState.AUTHENTICATED),
	REQUEST_EX_AUTO_FISH(0x104, ExRequestAutoFish::new, ConnectionState.IN_GAME),
	REQUEST_VIP_ATTENDANCE_ITEM_LIST(0x105, RequestVipAttendanceItemList::new, ConnectionState.IN_GAME),
	REQUEST_VIP_ATTENDANCE_CHECK(0x106, RequestVipAttendanceCheck::new, ConnectionState.IN_GAME),
	REQUEST_ITEM_ENSOUL(0x107, RequestItemEnsoul::new, ConnectionState.IN_GAME),
	REQUEST_CASTLE_WAR_SEASON_REWARD(0x108, null, ConnectionState.IN_GAME),
	REQUEST_VIP_PRODUCT_LIST(0x109, null, ConnectionState.IN_GAME),
	REQUEST_VIP_LUCKY_GAME_INFO(0x10A, RequestVipLuckGameInfo::new, ConnectionState.IN_GAME),
	REQUEST_VIP_LUCKY_GAME_ITEM_LIST(0x10B, null, ConnectionState.IN_GAME),
	REQUEST_VIP_LUCKY_GAME_BONUS(0x10C, null, ConnectionState.IN_GAME),
	EX_REQUEST_VIP_INFO(0x10D, ExRequestVipInfo::new, ConnectionState.IN_GAME),
	REQUEST_CAPTCHA_ANSWER(0x10E, RequestCaptchaAnswer::new, ConnectionState.IN_GAME),
	REQUEST_REFRESH_CAPTCHA_IMAGE(0x10F, RequestRefreshCaptcha::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_SIGN_IN_FOR_OPEN_JOINING_METHOD(0x110, RequestPledgeSignInForOpenJoiningMethod::new, ConnectionState.IN_GAME),
	EX_REQUEST_MATCH_ARENA(0x111, null, ConnectionState.IN_GAME),
	EX_CONFIRM_MATCH_ARENA(0x112, null, ConnectionState.IN_GAME),
	EX_CANCEL_MATCH_ARENA(0x113, null, ConnectionState.IN_GAME),
	EX_CHANGE_CLASS_ARENA(0x114, null, ConnectionState.IN_GAME),
	EX_CONFIRM_CLASS_ARENA(0x115, null, ConnectionState.IN_GAME),
	REQUEST_OPEN_DECO_NPCUI(0x116, null, ConnectionState.IN_GAME),
	REQUEST_CHECK_AGIT_DECO_AVAILABILITY(0x117, null, ConnectionState.IN_GAME),
	REQUEST_USER_FACTION_INFO(0x118, null, ConnectionState.IN_GAME),
	EX_EXIT_ARENA(0x119, null, ConnectionState.IN_GAME),
	REQUEST_EVENT_BALTHUS_TOKEN(0x11A, null, ConnectionState.IN_GAME),
	REQUEST_PARTY_MATCHING_HISTORY(0x11B, RequestPartyMatchingHistory::new, ConnectionState.IN_GAME),
	EX_ARENA_CUSTOM_NOTIFICATION(0x11C, null, ConnectionState.IN_GAME),
	REQUEST_TODO_LIST(0x11D, RequestTodoList::new, ConnectionState.IN_GAME),
	REQUEST_TODO_LIST_HTML(0x11E, null, ConnectionState.IN_GAME),
	REQUEST_ONE_DAY_REWARD_RECEIVE(0x11F, RequestOneDayRewardReceive::new, ConnectionState.IN_GAME),
	REQUEST_QUEUE_TICKET(0x120, null, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_BONUS_OPEN(0x121, RequestPledgeBonusOpen::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_BONUS_REWARD_LIST(0x122, RequestPledgeBonusRewardList::new, ConnectionState.IN_GAME),
	REQUEST_PLEDGE_BONUS_REWARD(0x123, RequestPledgeBonusReward::new, ConnectionState.IN_GAME),
	REQUEST_SSO_AUTHN_TOKEN(0x124, null, ConnectionState.IN_GAME),
	REQUEST_QUEUE_TICKET_LOGIN(0x125, null, ConnectionState.IN_GAME),
	REQUEST_BLOCK_MEMO_INFO(0x126, RequestBlockDetailInfo::new, ConnectionState.IN_GAME),
	REQUEST_TRY_EN_SOUL_EXTRACTION(0x127, RequestTryEnSoulExtraction::new, ConnectionState.IN_GAME),
	REQUEST_RAIDBOSS_SPAWN_INFO(0x128, RequestRaidBossSpawnInfo::new, ConnectionState.IN_GAME),
	REQUEST_RAID_SERVER_INFO(0x129, RequestRaidServerInfo::new, ConnectionState.IN_GAME),
	REQUEST_SHOW_AGIT_SIEGE_INFO(0x12A, null, ConnectionState.IN_GAME),
	REQUEST_ITEM_AUCTION_STATUS(0x12B, null, ConnectionState.IN_GAME),
	REQUEST_MONSTER_BOOK_OPEN(0x12C, null, ConnectionState.IN_GAME),
	REQUEST_MONSTER_BOOK_CLOSE(0x12D, null, ConnectionState.IN_GAME),
	REQUEST_MONSTER_BOOK_REWARD(0x12E, null, ConnectionState.IN_GAME),
	EXREQUEST_MATCH_GROUP(0x12F, null, ConnectionState.IN_GAME),
	EXREQUEST_MATCH_GROUP_ASK(0x130, null, ConnectionState.IN_GAME),
	EXREQUEST_MATCH_GROUP_ANSWER(0x131, null, ConnectionState.IN_GAME),
	EXREQUEST_MATCH_GROUP_WITHDRAW(0x132, null, ConnectionState.IN_GAME),
	EXREQUEST_MATCH_GROUP_OUST(0x133, null, ConnectionState.IN_GAME),
	EXREQUEST_MATCH_GROUP_CHANGE_MASTER(0x134, null, ConnectionState.IN_GAME),
	REQUEST_UPGRADE_SYSTEM_RESULT(0x135, RequestUpgradeSystemResult::new, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_PICK_NUMB(0x136, null, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_GAME_REWARD_REQUEST(0x137, null, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_GAME_RETRY(0x138, null, ConnectionState.IN_GAME),
	EX_CARD_UPDOWN_GAME_QUIT(0x139, null, ConnectionState.IN_GAME),
	EX_ARENA_RANK_ALL(0x13A, null, ConnectionState.IN_GAME),
	EX_ARENA_MYRANK(0x13B, null, ConnectionState.IN_GAME),
	EX_SWAP_AGATHION_SLOT_ITEMS(0x13C, null, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_RANK(0x13D, null, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_INFO(0x13E, null, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_REWARD(0x13F, null, ConnectionState.IN_GAME),
	EX_PLEDGE_LEVEL_UP(0x140, null, ConnectionState.IN_GAME),
	EX_PLEDGE_MISSION_INFO(0x141, null, ConnectionState.IN_GAME),
	EX_PLEDGE_MISSION_REWARD(0x142, null, ConnectionState.IN_GAME),
	EX_PLEDGE_MASTERY_INFO(0x143, null, ConnectionState.IN_GAME),
	EX_PLEDGE_MASTERY_SET(0x144, null, ConnectionState.IN_GAME),
	EX_PLEDGE_MASTERY_RESET(0x145, null, ConnectionState.IN_GAME),
	EX_PLEDGE_SKILL_INFO(0x146, null, ConnectionState.IN_GAME),
	EX_PLEDGE_SKILL_ACTIVATE(0x147, null, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_LIST(0x148, null, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_ACTIVATE(0x149, null, ConnectionState.IN_GAME),
	EX_PLEDGE_ANNOUNCE(0x14A, null, ConnectionState.IN_GAME),
	EX_PLEDGE_ANNOUNCE_SET(0x14B, null, ConnectionState.IN_GAME),
	EX_CREATE_PLEDGE(0x14C, null, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_INFO(0x14D, null, ConnectionState.IN_GAME),
	EX_PLEDGE_ITEM_BUY(0x14E, null, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_INFO(0x14F, ExElementalSpiritInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EXTRACT_INFO(0x150, ExElementalSpiritExtractInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EXTRACT(0x151, ExElementalSpiritExtract::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EVOLUTION_INFO(0x152, ExElementalSpiritEvolutionInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_EVOLUTION(0x153, ExElementalSpiritEvolution::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_SET_TALENT(0x154, ExElementalSpiritSetTalent::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_INIT_TALENT(0x155, ExElementalInitTalent::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_ABSORB_INFO(0x156, ExElementalSpiritAbsorbInfo::new, ConnectionState.IN_GAME),
	EX_ELEMENTAL_SPIRIT_ABSORB(0x157, ExElementalSpiritAbsorb::new, ConnectionState.IN_GAME),
	EX_REQUEST_LOCKED_ITEM(0x158, null, ConnectionState.IN_GAME),
	EX_REQUEST_UNLOCKED_ITEM(0x159, null, ConnectionState.IN_GAME),
	EX_LOCKED_ITEM_CANCEL(0x15A, null, ConnectionState.IN_GAME),
	EX_UNLOCKED_ITEM_CANCEL(0x15B, null, ConnectionState.IN_GAME),
	// 152
	EX_BLOCK_PACKET_FOR_AD(0x15C, null, ConnectionState.IN_GAME),
	EX_USER_BAN_INFO(0x15D, null, ConnectionState.IN_GAME),
	EX_INTERACT_MODIFY(0x15E, ExInteractModify::new, ConnectionState.IN_GAME),
	EX_TRY_ENCHANT_ARTIFACT(0x15F, null, ConnectionState.IN_GAME),
	EX_UPGRADE_SYSTEM_NORMAL_REQUEST(0x160, ExUpgradeSystemNormalRequest::new, ConnectionState.IN_GAME),
	EX_PURCHASE_LIMIT_SHOP_ITEM_LIST(0x161, RequestPurchaseLimitShopItemList::new, ConnectionState.IN_GAME),
	EX_PURCHASE_LIMIT_SHOP_ITEM_BUY(0x162, RequestPurchaseLimitShopItemBuy::new, ConnectionState.IN_GAME),
	// 228
	EX_OPEN_HTML(0x163, ExOpenHtml::new, ConnectionState.IN_GAME),
	EX_REQUEST_CLASS_CHANGE(0x164, ExRequestClassChange::new, ConnectionState.IN_GAME),
	EX_REQUEST_CLASS_CHANGE_VERIFYING(0x165, ExRequestClassChangeVerifying::new, ConnectionState.IN_GAME),
	EX_REQUEST_TELEPORT(0x166, ExRequestTeleport::new, ConnectionState.IN_GAME),
	EX_COSTUME_USE_ITEM(0x167, null, ConnectionState.IN_GAME),
	EX_COSTUME_LIST(0x168, null, ConnectionState.IN_GAME),
	EX_COSTUME_COLLECTION_SKILL_ACTIVE(0x169, null, ConnectionState.IN_GAME),
	EX_COSTUME_EVOLUTION(0x16A, null, ConnectionState.IN_GAME),
	EX_COSTUME_EXTRACT(0x16B, null, ConnectionState.IN_GAME),
	EX_COSTUME_LOCK(0x16C, null, ConnectionState.IN_GAME),
	EX_COSTUME_CHANGE_SHORTCUT(0x16D, null, ConnectionState.IN_GAME),
	EX_MAGICLAMP_GAME_INFO(0x16E, ExMagicLampGameInfo::new, ConnectionState.IN_GAME),
	EX_MAGICLAMP_GAME_START(0x16F, ExMagicLampGameStart::new, ConnectionState.IN_GAME),
	EX_ACTIVATE_AUTO_SHORTCUT(0x170, ExRequestActivateAutoShortcut::new, ConnectionState.IN_GAME),
	EX_PREMIUM_MANAGER_LINK_HTML(0x171, null, ConnectionState.IN_GAME),
	EX_PREMIUM_MANAGER_PASS_CMD_TO_SERVER(0x172, null, ConnectionState.IN_GAME),
	EX_ACTIVATED_CURSED_TREASURE_BOX_LOCATION(0x173, null, ConnectionState.IN_GAME),
	EX_PAYBACK_LIST(0x174, null, ConnectionState.IN_GAME),
	EX_PAYBACK_GIVE_REWARD(0x175, null, ConnectionState.IN_GAME),
	EX_AUTOPLAY_SETTING(0x176, ExAutoPlaySetting::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_MATCH_MAKING(0x177, OlympiadMatchMaking::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_MATCH_MAKING_CANCEL(0x178, OlympiadMatchMakingCancel::new, ConnectionState.IN_GAME),
	EX_FESTIVAL_BM_INFO(0x179, null, ConnectionState.IN_GAME),
	EX_FESTIVAL_BM_GAME(0x17A, null, ConnectionState.IN_GAME),
	EX_GACHA_SHOP_INFO(0x17B, null, ConnectionState.IN_GAME),
	EX_GACHA_SHOP_GACHA_GROUP(0x17C, null, ConnectionState.IN_GAME),
	EX_GACHA_SHOP_GACHA_ITEM(0x17D, null, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_LIST(0x17E, ExTimedHuntingZoneList::new, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_USER_ENTER(0x17F, ExTimedHuntingZoneEnter::new, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_USER_LEAVE(0x180, ExTimedHuntingZoneLeave::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_INFO(0x181, RequestRankingCharInfo::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_HISTORY(0x182, RequestRankingCharHistory::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_RANKERS(0x183, RequestRankingCharRankers::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_SPAWN_BUFFZONE_NPC(0x184, RequestExRankingCharSpawnBuffzoneNpc::new, ConnectionState.IN_GAME),
	EX_RANKING_CHAR_BUFFZONE_NPC_POSITION(0x185, RequestExRankingCharBuffzoneNpcPosition::new, ConnectionState.IN_GAME),
	EX_PLEDGE_MERCENARY_RECRUIT_INFO_SET(0x186, ExPledgeMercenaryRecruitInfoSet::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_INFO(0x187, ExMercenaryCastleWarCastleSiegeInfo::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_INFO(0x188, ExMercenaryCastleWarCastleSiegeHudInfo::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST(0x189, ExMercenaryCastleWarCastleSiegeAttackerList::new, ConnectionState.IN_GAME),
	EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_DEFENDER_LIST(0x18A, ExMercenaryCastleWarCastleSiegeDefenderList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_MERCENARY_MEMBER_LIST(0x18B, ExPledgeMercenaryMemberList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_MERCENARY_MEMBER_JOIN(0x18C, ExPledgeMercenaryMemberJoin::new, ConnectionState.IN_GAME),
	EX_PVP_BOOK_LIST(0x18D, ExPvpBookList::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_KILLER_LOCATION(0x18E, null, ConnectionState.IN_GAME),
	EX_PVPBOOK_TELEPORT_TO_KILLER(0x18F, null, ConnectionState.IN_GAME),
	EX_LETTER_COLLECTOR_TAKE_REWARD(0x190, ExLetterCollectorTakeReward::new, ConnectionState.IN_GAME),
	EX_SET_STATUS_BONUS(0x191, ExSetStatusBonus::new, ConnectionState.IN_GAME),
	EX_RESET_STATUS_BONUS(0x192, ExResetStatusBonus::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_MY_RANKING_INFO(0x193, RequestOlympiadMyRankingInfo::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_RANKING_INFO(0x194, RequestOlympiadRankingInfo::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_HERO_AND_LEGEND_INFO(0x195, RequestOlympiadHeroAndLegendInfo::new, ConnectionState.IN_GAME),
	EX_CASTLEWAR_OBSERVER_START(0x196, ExCastleWarObserverStart::new, ConnectionState.IN_GAME),
	EX_RAID_TELEPORT_INFO(0x197, RequestRaidTeleportInfo::new, ConnectionState.IN_GAME),
	EX_TELEPORT_TO_RAID_POSITION(0x198, ExTeleportToRaidPosition::new, ConnectionState.IN_GAME),
	EX_CRAFT_EXTRACT(0x199, ExRequestRandomCraftExtract::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_INFO(0x19A, ExRequestRandomCraftInfo::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_LOCK_SLOTEX_CRAFT_RANDOM_INFO(0x19B, ExRequestRandomCraftLockSlot::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_REFRESH(0x19C, ExRequestRandomCraftRefresh::new, ConnectionState.IN_GAME),
	EX_CRAFT_RANDOM_MAKE(0x19D, ExRequestRandomCraftMake::new, ConnectionState.IN_GAME),
	EX_MULTI_SELL_LIST(0x19E, RequestMultisellList::new, ConnectionState.IN_GAME),
	EX_SAVE_ITEM_ANNOUNCE_SETTING(0x19F, ExSaveItemAnnounceSetting::new, ConnectionState.IN_GAME),
	EX_OLYMPIAD_UI(0x1A0, OlympiadUI::new, ConnectionState.IN_GAME),
	// 270
	EX_SHARED_POSITION_SHARING_UI(0x1A1, ExRequestSharingLocationUi::new, ConnectionState.IN_GAME),
	EX_SHARED_POSITION_TELEPORT_UI(0x1A2, ExRequestSharedLocationTeleportUi::new, ConnectionState.IN_GAME),
	EX_SHARED_POSITION_TELEPORT(0x1A3, ExRequestSharedLocationTeleport::new, ConnectionState.IN_GAME),
	EX_AUTH_RECONNECT(0x1A4, null, ConnectionState.IN_GAME),
	EX_PET_EQUIP_ITEM(0x1A5, ExPetEquipItem::new, ConnectionState.IN_GAME),
	EX_PET_UNEQUIP_ITEM(0x1A6, ExPetUnequipItem::new, ConnectionState.IN_GAME),
	EX_SHOW_HOMUNCULUS_INFO(0x1A7, null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_CREATE_START(0x1A8, null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_INSERT(0x1A9, null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_SUMMON(0x1AA, null, ConnectionState.IN_GAME),
	EX_DELETE_HOMUNCULUS_DATA(0x1AB, null, ConnectionState.IN_GAME),
	EX_REQUEST_ACTIVATE_HOMUNCULUS(0x1AC, null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_GET_ENCHANT_POINT(0x1AD, null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_INIT_POINT(0x1AE, null, ConnectionState.IN_GAME),
	EX_EVOLVE_PET(0x1AF, ExEvolvePet::new, ConnectionState.IN_GAME),
	EX_ENCHANT_HOMUNCULUS_SKILL(0x1B0, null, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_ENCHANT_EXP(0x1B1, null, ConnectionState.IN_GAME),
	EX_TELEPORT_FAVORITES_LIST(0x1B2, ExRequestTeleportFavoriteList::new, ConnectionState.IN_GAME),
	EX_TELEPORT_FAVORITES_UI_TOGGLE(0x1B3, ExRequestTeleportFavoritesUIToggle::new, ConnectionState.IN_GAME),
	EX_TELEPORT_FAVORITES_ADD_DEL(0x1B4, ExRequestTeleportFavoritesAddDel::new, ConnectionState.IN_GAME),
	EX_ANTIBOT(0x1B5, null, ConnectionState.IN_GAME),
	EX_DPSVR(0x1B6, null, ConnectionState.IN_GAME),
	EX_TENPROTECT_DECRYPT_ERROR(0x1B7, null, ConnectionState.IN_GAME),
	EX_NET_LATENCY(0x1B8, null, ConnectionState.IN_GAME),
	EX_MABLE_GAME_OPEN(0x1B9, ExRequestMableGameOpen::new, ConnectionState.IN_GAME),
	EX_MABLE_GAME_ROLL_DICE(0x1BA, ExRequestMableGameRollDice::new, ConnectionState.IN_GAME),
	EX_MABLE_GAME_POPUP_OK(0x1BB, ExRequestMableGamePopupOk::new, ConnectionState.IN_GAME),
	EX_MABLE_GAME_RESET(0x1BC, ExRequestMableGameReset::new, ConnectionState.IN_GAME),
	EX_MABLE_GAME_CLOSE(0x1BD, ExRequestMableGameClose::new, ConnectionState.IN_GAME),
	EX_RETURN_TO_ORIGIN(0x1BE, null, ConnectionState.IN_GAME),
	EX_PK_PENALTY_LIST(0x1BF, null, ConnectionState.IN_GAME),
	EX_PK_PENALTY_LIST_ONLY_LOC(0x1C0, null, ConnectionState.IN_GAME),
	EX_BLESS_OPTION_PUT_ITEM(0x1C1, RequestBlessOptionPutItem::new, ConnectionState.IN_GAME),
	EX_BLESS_OPTION_ENCHANT(0x1C2, RequestBlessOptionEnchant::new, ConnectionState.IN_GAME),
	EX_BLESS_OPTION_CANCEL(0x1C3, RequestBlessOptionCancel::new, ConnectionState.IN_GAME),
	EX_PVP_RANKING_MY_INFO(0x1C4, RequestPvpRankingMyInfo::new, ConnectionState.IN_GAME),
	EX_PVP_RANKING_LIST(0x1C5, RequestPvpRankingList::new, ConnectionState.IN_GAME),
	EX_ACQUIRE_PET_SKILL(0x1C6, RequestExAcquirePetSkill::new, ConnectionState.IN_GAME),
	EX_PLEDGE_V3_INFO(0x1C7, RequestExPledgeV3Info::new, ConnectionState.IN_GAME),
	EX_PLEDGE_ENEMY_INFO_LIST(0x1C8, RequestExPledgeEnemyInfoList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_ENEMY_REGISTER(0x1C9, RequestExPledgeEnemyRegister::new, ConnectionState.IN_GAME),
	EX_PLEDGE_ENEMY_DELETE(0x1CA, RequestExPledgeEnemyDelete::new, ConnectionState.IN_GAME),
	EX_TRY_PET_EXTRACT_SYSTEM(0x1CB, ExTryPetExtractSystem::new, ConnectionState.IN_GAME),
	EX_PLEDGE_V3_SET_ANNOUNCE(0x1CC, RequestExPledgeV3SetAnnounce::new, ConnectionState.IN_GAME),
	// 306
	EX_RANKING_FESTIVAL_OPEN(0x1CD, null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_BUY(0x1CE, null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_BONUS(0x1CF, null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_RANKING(0x1D0, null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_MY_RECEIVED_BONUS(0x1D1, null, ConnectionState.IN_GAME),
	EX_RANKING_FESTIVAL_REWARD(0x1D2, null, ConnectionState.IN_GAME),
	EX_TIMER_CHECK(0x1D3, null, ConnectionState.IN_GAME),
	EX_STEADY_BOX_LOAD(0x1D4, RequestSteadyBoxLoad::new, ConnectionState.IN_GAME),
	EX_STEADY_OPEN_SLOT(0x1D5, RequestSteadyOpenSlot::new, ConnectionState.IN_GAME),
	EX_STEADY_OPEN_BOX(0x1D6, RequestSteadyOpenBox::new, ConnectionState.IN_GAME),
	EX_STEADY_GET_REWARD(0x1D7, RequestSteadyGetReward::new, ConnectionState.IN_GAME),
	EX_PET_RANKING_MY_INFO(0x1D8, RequestPetRankingMyInfo::new, ConnectionState.IN_GAME),
	EX_PET_RANKING_LIST(0x1D9, RequestPetRankingList::new, ConnectionState.IN_GAME),
	EX_COLLECTION_OPEN_UI(0x1DA, RequestExCollectionOpenUI::new, ConnectionState.IN_GAME),
	EX_COLLECTION_CLOSE_UI(0x1DB, RequestCollectionCloseUI::new, ConnectionState.IN_GAME),
	EX_COLLECTION_LIST(0x1DC, RequestExCollectionList::new, ConnectionState.IN_GAME),
	EX_COLLECTION_UPDATE_FAVORITE(0x1DD, RequestCollectionUpdateFavorite::new, ConnectionState.IN_GAME),
	EX_COLLECTION_FAVORITE_LIST(0x1DE, RequestCollectionFavoriteList::new, ConnectionState.IN_GAME),
	EX_COLLECTION_SUMMARY(0x1DF, RequestExCollectionSummary::new, ConnectionState.IN_GAME),
	EX_COLLECTION_REGISTER(0x1E0, RequestCollectionRegister::new, ConnectionState.IN_GAME),
	EX_COLLECTION_RECEIVE_REWARD(0x1E1, RequestCollectionReceiveReward::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_LIST(0x1E2, RequestExPvpBookShareRevengeList::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_REQ_SHARE_REVENGEINFO(0x1E3, RequestExPvpBookShareRevengeReqShareRevengeInfo::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_KILLER_LOCATION(0x1E4, RequestExPvpBookShareRevengeKillerLocation::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_TELEPORT_TO_KILLER(0x1E5, RequestExPvpBookShareRevengeTeleportToKiller::new, ConnectionState.IN_GAME),
	EX_PVPBOOK_SHARE_REVENGE_SHARED_TELEPORT_TO_KILLER(0x1E6, RequestExPvpBookShareRevengeSharedTeleportToKiller::new, ConnectionState.IN_GAME),
	EX_PENALTY_ITEM_LIST(0x1E7, null, ConnectionState.IN_GAME),
	EX_PENALTY_ITEM_RESTORE(0x1E8, null, ConnectionState.IN_GAME),
	EX_USER_WATCHER_TARGET_LIST(0x1E9, ExRequestUserWatcherTargetList::new, ConnectionState.IN_GAME),
	EX_USER_WATCHER_ADD(0x1EA, ExRequestUserWatcherAdd::new, ConnectionState.IN_GAME),
	EX_USER_WATCHER_DELETE(0x1EB, ExRequestUserWatcherDelete::new, ConnectionState.IN_GAME),
	EX_HOMUNCULUS_ACTIVATE_SLOT(0x1EC, null, ConnectionState.IN_GAME),
	EX_SUMMON_HOMUNCULUS_COUPON(0x1ED, null, ConnectionState.IN_GAME),
	EX_SUBJUGATION_LIST(0x1EE, RequestSubjugationList::new, ConnectionState.IN_GAME),
	EX_SUBJUGATION_RANKING(0x1EF, RequestSubjugationRanking::new, ConnectionState.IN_GAME),
	EX_SUBJUGATION_GACHA_UI(0x1F0, RequestSubjugationGachaUI::new, ConnectionState.IN_GAME),
	EX_SUBJUGATION_GACHA(0x1F1, RequestSubjugationGacha::new, ConnectionState.IN_GAME),
	EX_PLEDGE_DONATION_INFO(0x1F2, RequestExPledgeDonationInfo::new, ConnectionState.IN_GAME),
	EX_PLEDGE_DONATION_REQUEST(0x1F3, RequestExPledgeDonationRequest::new, ConnectionState.IN_GAME),
	EX_PLEDGE_CONTRIBUTION_LIST(0x1F4, RequestExPledgeContributionList::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RANKING_MY_INFO(0x1F5, RequestPledgeRankingMyInfo::new, ConnectionState.IN_GAME),
	EX_PLEDGE_RANKING_LIST(0x1F6, RequestPledgeRankingList::new, ConnectionState.IN_GAME),
	EX_ITEM_RESTORE_LIST(0x1F7, null, ConnectionState.IN_GAME),
	EX_ITEM_RESTORE(0x1F8, null, ConnectionState.IN_GAME),
	// 338
	EX_DETHRONE_INFO(0x1F9, null, ConnectionState.IN_GAME),
	EX_DETHRONE_RANKING_INFO(0x1FA, null, ConnectionState.IN_GAME),
	EX_DETHRONE_SERVER_INFO(0x1FB, null, ConnectionState.IN_GAME),
	EX_DETHRONE_DISTRICT_OCCUPATION_INFO(0x1FC, null, ConnectionState.IN_GAME),
	EX_DETHRONE_DAILY_MISSION_INFO(0x1FD, null, ConnectionState.IN_GAME),
	EX_DETHRONE_DAILY_MISSION_GET_REWARD(0x1FE, null, ConnectionState.IN_GAME),
	EX_DETHRONE_PREV_SEASON_INFO(0x1FF, null, ConnectionState.IN_GAME),
	EX_DETHRONE_GET_REWARD(0x200, null, ConnectionState.IN_GAME),
	EX_DETHRONE_ENTER(0x201, null, ConnectionState.IN_GAME),
	EX_DETHRONE_LEAVE(0x202, null, ConnectionState.IN_GAME),
	EX_DETHRONE_CHECK_NAME(0x203, null, ConnectionState.IN_GAME),
	EX_DETHRONE_CHANGE_NAME(0x204, null, ConnectionState.IN_GAME),
	EX_DETHRONE_CONNECT_CASTLE(0x205, null, ConnectionState.IN_GAME),
	EX_DETHRONE_DISCONNECT_CASTLE(0x206, null, ConnectionState.IN_GAME),
	EX_CHANGE_NICKNAME_COLOR_ICON(0x207, RequestChangeNicknameEmote::new, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_MOVE_TO_HOST(0x208, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_RETURN_TO_ORIGIN_PEER(0x209, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_INFO(0x20A, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_INFO(0x20B, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_JOIN(0x20C, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST(0x20D, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_RECRUIT_INFO_SET(0x20E, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_LIST(0x20F, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_JOIN(0x210, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_TELEPORT(0x211, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_OBSERVER_START(0x212, null, ConnectionState.IN_GAME),
	EX_PRIVATE_STORE_SEARCH_LIST(0x213, ExRequestPrivateStoreSearchList::new, ConnectionState.IN_GAME),
	EX_PRIVATE_STORE_SEARCH_STATISTICS(0x214, ExRequestPrivateStoreSearchStatistics::new, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_RANKING_INFO(0x215, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_RANKING_INFO(0x216, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_HUD_INFO(0x217, null, ConnectionState.IN_GAME),
	EX_NEW_HENNA_LIST(0x218, RequestNewHennaList::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_EQUIP(0x219, RequestNewHennaEquip::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_UNEQUIP(0x21A, RequestNewHennaUnequip::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_POTEN_SELECT(0x21B, RequestNewHennaPotenSelect::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_POTEN_ENCHANT(0x21C, RequestNewHennaPotenEnchant::new, ConnectionState.IN_GAME),
	EX_NEW_HENNA_COMPOSE(0x21D, RequestNewHennaCompose::new, ConnectionState.IN_GAME),
	EX_REQUEST_INVITE_PARTY(0x21E, RequestNewInvitePartyInquiry::new, ConnectionState.IN_GAME),
	EX_ITEM_USABLE_LIST(0x21F, null, ConnectionState.IN_GAME),
	EX_PACKETREADCOUNTPERSECOND(0x220, null, ConnectionState.IN_GAME),
	EX_SELECT_GLOBAL_EVENT_UI(0x221, null, ConnectionState.IN_GAME),
	EX_L2PASS_INFO(0x222, RequestHuntPassInfo::new, ConnectionState.IN_GAME),
	EX_L2PASS_REQUEST_REWARD(0x223, RequestHuntPassReward::new, ConnectionState.IN_GAME),
	EX_L2PASS_REQUEST_REWARD_ALL(0x224, RequestHuntPassRewardAll::new, ConnectionState.IN_GAME),
	EX_L2PASS_BUY_PREMIUM(0x225, RequestHuntPassBuyPremium::new, ConnectionState.IN_GAME),
	EX_SAYHAS_SUPPORT_TOGGLE(0x226, HuntpassSayhasToggle::new, ConnectionState.IN_GAME),
	// 362
	EX_REQ_ENCHANT_FAIL_REWARD_INFO(0x227, ExRequestEnchantFailRewardInfo::new, ConnectionState.IN_GAME),
	EX_SET_ENCHANT_CHALLENGE_POINT(0x228, ExRequestSetEnchantChallengePoint::new, ConnectionState.IN_GAME),
	EX_RESET_ENCHANT_CHALLENGE_POINT(0x229, ExRequestResetEnchantChallengePoint::new, ConnectionState.IN_GAME),
	EX_REQ_VIEW_ENCHANT_RESULT(0x22A, ExRequestViewEnchantResult::new, ConnectionState.IN_GAME),
	EX_REQ_START_MULTI_ENCHANT_SCROLL(0x22B, ExRequestStartMultiEnchantScroll::new, ConnectionState.IN_GAME),
	EX_REQ_VIEW_MULTI_ENCHANT_RESULT(0x22C, ExRequestViewMultiEnchantResult::new, ConnectionState.IN_GAME),
	EX_REQ_FINISH_MULTI_ENCHANT_SCROLL(0x22D, ExRequestFinishMultiEnchantScroll::new, ConnectionState.IN_GAME),
	EX_REQ_CHANGE_MULTI_ENCHANT_SCROLL(0x22E, null, ConnectionState.IN_GAME),
	EX_REQ_SET_MULTI_ENCHANT_ITEM_LIST(0x22F, ExRequestSetMultiEnchantItemList::new, ConnectionState.IN_GAME),
	EX_REQ_MULTI_ENCHANT_ITEM_LIST(0x230, ExRequestMultiEnchantItemList::new, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_SUPPORT_PLEDGE_FLAG_SET(0x231, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_SUPPORT_PLEDGE_INFO_SET(0x232, null, ConnectionState.IN_GAME),
	EX_REQ_HOMUNCULUS_PROB_LIST(0x233, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_ALL_RANKING_INFO(0x234, null, ConnectionState.IN_GAME),
	EX_WORLDCASTLEWAR_CASTLE_SIEGE_ALL_RANKING_INFO(0x235, null, ConnectionState.IN_GAME),
	EX_MISSION_LEVEL_REWARD_LIST(0x236, RequestMissionRewardList::new, ConnectionState.IN_GAME),
	EX_MISSION_LEVEL_RECEIVE_REWARD(0x237, RequestMissionLevelReceiveReward::new, ConnectionState.IN_GAME),
	EX_BALROGWAR_TELEPORT(0x238, ExBalrogWarTeleport::new, ConnectionState.IN_GAME),
	EX_BALROGWAR_SHOW_UI(0x239, ExBalrogWarShowUI::new, ConnectionState.IN_GAME),
	EX_BALROGWAR_SHOW_RANKING(0x23A, ExBalrogWarShowRanking::new, ConnectionState.IN_GAME),
	EX_BALROGWAR_GET_REWARD(0x23B, ExBalrogWarGetReward::new, ConnectionState.IN_GAME),
	EX_USER_RESTART_LOCKER_UPDATE(0x23C, null, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_ITEM_LIST(0x23D, ExWorldExchangeItemList::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_REGI_ITEM(0x23E, ExWorldExchangeRegisterItem::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_BUY_ITEM(0x23F, ExWorldExchangeBuyItem::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_SETTLE_LIST(0x240, ExWorldExchangeSettleList::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_SETTLE_RECV_RESULT(0x241, ExWorldExchangeSettleRecvResult::new, ConnectionState.IN_GAME),
	EX_READY_ITEM_AUTO_PEEL(0x242, ExRequestReadyItemAutoPeel::new, ConnectionState.IN_GAME),
	EX_REQUEST_ITEM_AUTO_PEEL(0x243, ExRequestItemAutoPeel::new, ConnectionState.IN_GAME),
	EX_STOP_ITEM_AUTO_PEEL(0x244, ExRequestStopItemAutoPeel::new, ConnectionState.IN_GAME),
	EX_VARIATION_OPEN_UI(0x245, ExVariationOpenUi::new, ConnectionState.IN_GAME),
	EX_VARIATION_CLOSE_UI(0x246, ExVariationCloseUi::new, ConnectionState.IN_GAME),
	EX_APPLY_VARIATION_OPTION(0x247, ExApplyVariationOption::new, ConnectionState.IN_GAME),
	EX_REQUEST_AUDIO_LOG_SAVE(0x248, null, ConnectionState.IN_GAME),
	EX_BR_VERSION(0x249, RequestBRVersion::new, ConnectionState.AUTHENTICATED, ConnectionState.CONNECTED),
	// 388
	EX_WRANKING_FESTIVAL_INFO(0x24A, null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_OPEN(0x24B, null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_BUY(0x24C, null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_BONUS(0x24D, null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_RANKING(0x24E, null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_MY_RECEIVED_BONUS(0x24F, null, ConnectionState.IN_GAME),
	EX_WRANKING_FESTIVAL_REWARD(0x250, null, ConnectionState.IN_GAME),
	EX_HENNA_UNEQUIP_INFO(0x251, RequestNewHennaUnequipInfo::new, ConnectionState.IN_GAME),
	EX_HERO_BOOK_CHARGE(0x252, null, ConnectionState.IN_GAME),
	EX_HERO_BOOK_ENCHANT(0x253, null, ConnectionState.IN_GAME),
	EX_TELEPORT_UI(0x254, null, ConnectionState.IN_GAME),
	EX_GOODS_GIFT_LIST_INFO(0x255, null, ConnectionState.IN_GAME),
	EX_GOODS_GIFT_ACCEPT(0x256, null, ConnectionState.IN_GAME),
	EX_GOODS_GIFT_REFUSE(0x257, null, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_AVERAGE_PRICE(0x258, ExWorldExchangeAveragePrice::new, ConnectionState.IN_GAME),
	EX_WORLD_EXCHANGE_TOTAL_LIST(0x259, ExWorldExchangeTotalList::new, ConnectionState.IN_GAME),
	EX_PRISON_USER_INFO(0x25A, null, ConnectionState.IN_GAME),
	EX_PRISON_USER_DONATION(0x25B, null, ConnectionState.IN_GAME),
	// 414
	EX_TRADE_LIMIT_INFO(0x25C, null, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_OPEN(0x25D, ExUniqueGachaOpen::new, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_GAME(0x25E, ExUniqueGachaGame::new, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_INVEN_ITEM_LIST(0x25F, ExUniqueGachaInvenItemList::new, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_INVEN_GET_ITEM(0x260, ExUniqueGachaInvenGetItem::new, ConnectionState.IN_GAME),
	EX_UNIQUE_GACHA_HISTORY(0x261, ExUniqueGachaHistory::new, ConnectionState.IN_GAME),
	EX_SET_PLEDGE_CREST_PRESET(0x262, RequestSetPledgeCrestPreset::new, ConnectionState.IN_GAME),
	EX_GET_PLEDGE_CREST_PRESET(0x263, RequestGetPledgeCrestPreset::new, ConnectionState.IN_GAME),
	EX_DUAL_INVENTORY_SWAP(0x264, RequestExDualInventorySwap::new, ConnectionState.IN_GAME),
	EX_SP_EXTRACT_INFO(0x265, RequestExSpExtractInfo::new, ConnectionState.IN_GAME),
	EX_SP_EXTRACT_ITEM(0x266, RequestExSpExtractItem::new, ConnectionState.IN_GAME),
	EX_QUEST_TELEPORT(0x267, RequestExQuestTeleport::new, ConnectionState.IN_GAME),
	EX_QUEST_ACCEPT(0x268, RequestExQuestAccept::new, ConnectionState.IN_GAME),
	EX_QUEST_CANCEL(0x269, RequestExQuestCancel::new, ConnectionState.IN_GAME),
	EX_QUEST_COMPLETE(0x26A, RequestExQuestComplete::new, ConnectionState.IN_GAME),
	EX_QUEST_NOTIFICATION_ALL(0x26B, RequestExQuestNotificationAll::new, ConnectionState.IN_GAME),
	EX_QUEST_UI(0x26C, RequestExQuestUI::new, ConnectionState.IN_GAME),
	EX_QUEST_ACCEPTABLE_LIST(0x26D, RequestExQuestAcceptableList::new, ConnectionState.IN_GAME),
	EX_SKILL_ENCHANT_INFO(0x26E, RequestExSkillEnchantInfo::new, ConnectionState.IN_GAME),
	EX_SKILL_ENCHANT_CHARGE(0x26F, RequestExSkillEnchantCharge::new, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_HOST_USER_ENTER(0x270, null, ConnectionState.IN_GAME),
	EX_TIME_RESTRICT_FIELD_HOST_USER_LEAVE(0x271, null, ConnectionState.IN_GAME),
	EX_DETHRONE_SHOP_OPEN_UI(0x272, null, ConnectionState.IN_GAME),
	EX_DETHRONE_SHOP_BUY(0x273, null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_OPEN_UI(0x274, null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_INIT(0x275, null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_EXP_UP(0x276, null, ConnectionState.IN_GAME),
	EX_ENHANCED_ABILITY_OF_FIRE_LEVEL_UP(0x277, null, ConnectionState.IN_GAME),
	EX_HOLY_FIRE_OPEN_UI(0x278, null, ConnectionState.IN_GAME),
	EX_PRIVATE_STORE_BUY_SELL(0x279, null, ConnectionState.IN_GAME),
	EX_MAX(0x27A, null, ConnectionState.IN_GAME);
	
	public static final ExClientPackets[] PACKET_ARRAY;
	static
	{
		final int maxPacketId = Arrays.stream(values()).mapToInt(ExClientPackets::getPacketId).max().orElse(0);
		PACKET_ARRAY = new ExClientPackets[maxPacketId + 1];
		for (ExClientPackets packet : values())
		{
			PACKET_ARRAY[packet.getPacketId()] = packet;
		}
	}
	
	private final int _packetId;
	private final Supplier<ClientPacket> _packetSupplier;
	private final Set<ConnectionState> _connectionStates;
	
	ExClientPackets(int packetId, Supplier<ClientPacket> packetSupplier, ConnectionState... connectionStates)
	{
		// Packet id is an unsigned short.
		if (packetId > 0xFFFF)
		{
			throw new IllegalArgumentException("Packet id must not be bigger than 0xFFFF");
		}
		
		_packetId = packetId;
		_packetSupplier = packetSupplier != null ? packetSupplier : () -> null;
		_connectionStates = new HashSet<>(Arrays.asList(connectionStates));
	}
	
	public int getPacketId()
	{
		return _packetId;
	}
	
	public ClientPacket newPacket()
	{
		final ClientPacket packet = _packetSupplier.get();
		if (Config.DEBUG_EX_CLIENT_PACKETS)
		{
			if (packet != null)
			{
				final String name = packet.getClass().getSimpleName();
				if (!Config.ALT_DEV_EXCLUDED_PACKETS.contains(name))
				{
					PacketLogger.info("[C EX] " + name);
				}
			}
			else if (Config.DEBUG_UNKNOWN_PACKETS)
			{
				PacketLogger.info("[C EX] 0x" + Integer.toHexString(_packetId).toUpperCase());
			}
		}
		return packet;
	}
	
	public Set<ConnectionState> getConnectionStates()
	{
		return _connectionStates;
	}
}
