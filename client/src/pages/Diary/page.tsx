import React, { useEffect, useState } from 'react';
import { Route, Routes, useNavigate, useParams } from "react-router-dom";
import DiaryMomPage from "@/pages/Diary/DiaryMom/page";
import DiaryBabyPage from "@/pages/Diary/DiaryBaby/page";
import { DiaryReadResponseProps, UserProps } from "@/pages/type/types";
import { fetchDiary } from "@/apis/diary/DiaryAPI";
import {MommyLetterWS} from "@/apis/ws/MommyLetterWS";

interface AccessUser{
    nickname:string;
    userId:string;
    role: string;
}
const DiaryPage=() => {
    const {param} = useParams();

    const currParam = param || "mom"; // 기본값 설정
    const [isToggled, setToggled] = useState(currParam === "baby");
    const navigate = useNavigate();
    const [accessUser, setAccessUser] = useState<AccessUser>()
    const [userId, setUserId] = useState<number>()
    const [diaryList, setDiaryList] = useState<DiaryReadResponseProps[]>([]);
    const paramUserId = useParams()["userId"];
    const [isAccess, setIsAccess] = useState<boolean>()
    //접근관련
    useEffect(() => {
        const headerUser = MommyLetterWS.getInstance().getUserInfo()
        setAccessUser(headerUser);
        if (!accessUser) return;
        if (Number(accessUser.userId) !== userId && accessUser.role !== "Doctor") setIsAccess( false);
        if (Number(accessUser.userId) === userId) setIsAccess(true);
    }, [userId]);

    useEffect(() => {
        setUserId(Number(paramUserId));
    }, [paramUserId]);
    useEffect(() => {
        if (currParam === "baby")
            setToggled(true);
    }, [currParam]);

    useEffect(() => {
        refreshDiary();
    }, [userId]);

    const refreshDiary = () => {
        if(!userId) return;
        fetchDiary(Number(userId))
            .then((data) => {
                setDiaryList(data);
            });
    };

    const handleToggle = () => {
        const newToggledState = !isToggled;
        setToggled(newToggledState);
        newToggledState ? navigate("baby") : navigate("mom");

    };


    return (
        <div className="h-[100%] w-[100%]">
            <section className="flex justify-between m-3">
                <span className="font-bold text-xl">{isToggled ? "육아일기" : "산모일기"}</span>
                <label className="swap">
                    <div className={`toggle-switch ${isToggled ? 'toggled' : ''}`} onClick={handleToggle}>
                        <div className="slider"></div>
                    </div>
                </label>
            </section>
            <Routes>

                <Route path={"/mom"} element={<DiaryMomPage diaryList={diaryList} setDiaryList={setDiaryList} userId={147} refreshDiary={refreshDiary}/>}/>
                <Route path={"/baby"} element={<DiaryBabyPage diaryList={diaryList} setDiaryList={setDiaryList} userId={147} refreshDiary={refreshDiary}/>}/>
            </Routes>
        </div>
    );
};

export default DiaryPage;
