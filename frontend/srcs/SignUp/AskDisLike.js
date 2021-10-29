import React, { useState } from "react";
import { Text, StyleSheet, TouchableOpacity, View } from "react-native";
import { SearchBar } from "./SearchBar";
import { SelectedFoodList } from "./SelectedFoodList";
import { postData } from "../func/func_data_communication";
import { ip } from "../data/data";
import { ObjectsInArrayToArray, arrayToObjectsInArray, arrayToObject } from "../func/func_change_var_type";


export const AskDisLike = ({ navigation, route}) => {
	const userinfo = route.params.userinfo;
	console.log("In D, userinfo: ", userinfo);
	const likeFoodList = route.params.likeFoodList;
	const [disLikeFoodList, setDisLikeFoodList] = useState(route.params.disLikeFoodList);
	console.log("disLikeFood: ", disLikeFoodList);
	const makePostData = (userinfo, likeFoodList, disLikeFoodList) => {
		let postData = {};
		let likeArr = likeFoodList.map((ele) => {
			return ele.food;
		});
		
		let dislikeArr = disLikeFoodList.length == 0 ? [] : disLikeFoodList.map((ele) => {
			return ele.food;
		});
		postData.email = userinfo.email;
		postData.nickName = userinfo.nickName;
		postData.password = userinfo.password;
		postData.likeFoodList = likeArr.join();
		postData.dislikeFoodList = dislikeArr.join();
		return postData;
	};

	console.log(makePostData(userinfo, likeFoodList, disLikeFoodList));
	
	const onPress = (item) => {
		const array = ObjectsInArrayToArray(disLikeFoodList);
		if (!array.includes(item.food)) {
			array.push(item.food);
			const newFoodList = arrayToObjectsInArray(array);
			setDisLikeFoodList(newFoodList);
		}
	};
	return (
		<View style={styles.container}>
			<Text
				style={{
					marginRight: "10%",
					fontSize: 35,
					fontFamily: "BlackHanSans_400Regular",
				}}
			>
				싫어하는 음식이 뭐야?
			</Text>
			<SearchBar
				onPress={onPress}
			/>
			<SelectedFoodList
				foodList={disLikeFoodList}
				setFoodList={setDisLikeFoodList}
			/>
			<View style={styles.buttonalign}>
				<TouchableOpacity
					style={{
						margin: "10%",
						backgroundColor: "orange",
						alignItems: "center",
						height: 55,
						width: 100,
						alignItems: "center",
						justifyContent: "center",
						borderRadius: 30,
					}}
					onPress={() => {
						navigation.navigate("AskLike", {
							userinfo: userinfo,
							disLikeFoodList: disLikeFoodList
						} );
					}}
				>
					<Text
						style={{
							fontSize: 20,
							color: "white",
							fontFamily: "BlackHanSans_400Regular",
						}}
					>
						이전
					</Text>
				</TouchableOpacity>
				<TouchableOpacity
					style={{
						margin: "10%",
						backgroundColor: "orange",
						alignItems: "center",
						height: 55,
						width: 100,
						alignItems: "center",
						justifyContent: "center",
						borderRadius: 30,
					}}
					onPress={() => {
						const data = makePostData(userinfo, likeFoodList, disLikeFoodList);
						postData(`${ip}/user/signup`, data);
						console.log("data: ", data);
						navigation.navigate("SignIn");
					}}
				>
					<Text
						style={{
							fontSize: 20,
							color: "white",
							fontFamily: "BlackHanSans_400Regular",
						}}
					>
						다음
					</Text>
				</TouchableOpacity>
			</View>
		</View>
	);
};

const styles = StyleSheet.create({
	container: {
		flex: 1,
		marginTop: "25%",
		alignItems: 'center'
	},
	buttonalign: {
		marginBottom: '20%',
		flexDirection: "row",
		margin: 10,
		alignItems: "center",
		justifyContent: "center",
	},
	seachbar: {
		// position: "relative",
		alignItems: "center",
		marginTop: "4%",
	},
});
