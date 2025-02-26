"use client";

import { useRouter } from "next/navigation"; // ✅ useRouter 추가
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import Image from "next/image";
import { ChevronRight } from "lucide-react";
import { components } from "@/lib/backend/generated/schema";
import dayjs from "dayjs";

const statusString = {
  ORDERED: "주문 완료", // 주문 완료 → 파란색
  READY_DELIVERY_SAME_DAY: "당일 배송 준비 완료", // 당일 배송 준비 → 노란색
  READY_DELIVERY_NEXT_DAY: "익일 백송 준비 완료", // 익일 배송 준비 → 주황색
  SHIPPED: "배송 출발", // 배송 출발 → 보라색
  DELIVERED: "배송 완료", // 배송 완료 → 회색
};

const statusColors = {
  ORDERED: "bg-blue-100 text-blue-700", // 주문 완료 → 파란색
  READY_DELIVERY_SAME_DAY: "bg-yellow-100 text-yellow-700", // 당일 배송 준비 → 노란색
  READY_DELIVERY_NEXT_DAY: "bg-orange-100 text-orange-700", // 익일 배송 준비 → 주황색
  SHIPPED: "bg-purple-100 text-purple-700", // 배송 출발 → 보라색
  DELIVERED: "bg-gray-100 text-gray-700", // 배송 완료 → 회색
};

export default function ClientPage({
  orders,
}: {
  orders: components["schemas"]["OrdersResponseBody"];
}) {
  const router = useRouter(); // ✅ 라우터 사용
  return (
    <div className="flex justify-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <div className="w-full max-w-screen-lg">
        <h2 className="text-2xl font-bold mb-8">내 주문 내역</h2>
        <div className="space-y-4">
          {orders.items?.map((order) => (
            <Card
              key={order.orderId}
              className="p-4 flex justify-between items-center hover:bg-gray-50 transition cursor-pointer"
              onClick={() => router.push(`/order/${order.orderId}`)} // ✅ 주문 상세 페이지로 이동
            >
              <div className="flex items-center gap-4">
                <div className="w-20 h-20 bg-gray-200 rounded-md flex items-center justify-center">
                  <Image
                    src={order.firstProductImageUrl || ""}
                    alt={order.firstProductName || ""}
                    width={70}
                    height={70}
                    className="rounded-md"
                    style={{ width: 70, height: 70 }}
                  />
                </div>
                <div>
                  <p className="text-sm text-gray-500">
                    주문일자:{" "}
                    {dayjs(order.orderDate).format(
                      "YYYY년 MM월 DD일 HH시 mm분"
                    )}
                  </p>
                  <p className="font-semibold">주문번호: {order.orderId}</p>
                  <p className="mt-1 text-gray-700">
                    {order.firstProductName}
                    {order.productCategoryCount > 1 &&
                      ` 외 ${order.productCategoryCount - 1}건`}
                  </p>
                  <p className="font-medium text-gray-600">
                    {order.totalPrice?.toLocaleString()}원
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-2">
                <Badge className={statusColors[order.orderStatus]}>
                  {statusString[order.orderStatus]}
                </Badge>
                <ChevronRight className="text-gray-400" />
              </div>
            </Card>
          ))}
        </div>

        <footer className="mt-12 text-center text-gray-500 text-sm">
          © 2024 Company Name. All rights reserved.
        </footer>
      </div>
    </div>
  );
}
