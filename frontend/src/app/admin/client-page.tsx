"use client";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/components/ui/table";
import { components } from "@/lib/backend/generated/schema";
import { LoginUserContext } from "@/stores/auth/auth-store";
import { Badge, Plus, ShoppingBag, ShoppingCart, Truck } from "lucide-react";
import { use, useEffect, useState } from "react";

export default function ClientPage() {
  const { isLogin, loginUser, isAdmin } = use(LoginUserContext);
  const [orderedCount, setOrderedCount] = useState(0);
  const [productCount, setProductCount] = useState(0);
  const [latestOrders, setLatestOrders] = useState<
    components["schemas"]["OrderResponse"][]
  >([]);

  async function initFetchInfo() {
    try {
      const orderedCountRes = await fetch("/api/admin/orders/ordered/count", {
        method: "GET",
      });
      if (!orderedCountRes.ok) return;
      const { data: orderedCount } = await orderedCountRes.json();
      setOrderedCount(orderedCount);

      const productCountRes = await fetch("/api/products/count", {
        method: "GET",
      });

      if (!productCountRes.ok) return;
      const { data: productCount } = await productCountRes.json();
      setProductCount(productCount);

      const latestOrdersRes = await fetch("/api/admin/orders/latest", {
        method: "GET",
      });

      if (!latestOrdersRes.ok) return;
      const { data: latestOrders } = await latestOrdersRes.json();
      setLatestOrders(latestOrders.items);
    } catch (err) {
      console.error("Fetch cart error:", err);
    }
  }

  useEffect(() => {
    initFetchInfo();
  }, []);

  const statusColors = {
    ORDERED: "bg-blue-100 text-blue-700", // 주문 완료 → 파란색
    READY_DELIVERY_SAME_DAY: "bg-yellow-100 text-yellow-700", // 당일 배송 준비 → 노란색
    READY_DELIVERY_NEXT_DAY: "bg-orange-100 text-orange-700", // 익일 배송 준비 → 주황색
    SHIPPED: "bg-purple-100 text-purple-700", // 배송 출발 → 보라색
    DELIVERED: "bg-gray-100 text-gray-700", // 배송 완료 → 회색
  };

  return (
    isLogin &&
    isAdmin && (
      <>
        <div className="flex flex-col w-full min-h-screen px-8 py-12 bg-gray-100">
          <div className="grid grid-cols-2 gap-6 mb-8">
            <Card className="p-6 flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">판매 중인 상품</p>
                <p className="text-3xl font-semibold">{productCount}</p>
              </div>
              <ShoppingBag className="text-gray-500 w-10 h-10" />
            </Card>

            <Card className="p-6 flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">대기 중인 주문</p>
                <p className="text-3xl font-semibold">{orderedCount}</p>
              </div>
              <ShoppingCart className="text-gray-500 w-10 h-10" />
            </Card>
          </div>

          {/* 최근 주문 */}
          <Card className="p-6">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-lg font-semibold">최근 주문</h3>
              <Button variant="outline">전체보기</Button>
            </div>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>주문번호</TableHead>
                  <TableHead>고객명</TableHead>
                  <TableHead>상태</TableHead>
                  <TableHead className="text-right">금액</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {latestOrders.map((order) => (
                  <TableRow key={order.orderId}>
                    <TableCell>#{order.orderId}</TableCell>
                    <TableCell>{order.username}</TableCell>
                    <TableCell>
                      <Badge className={statusColors[order.orderStatus]}>
                        {order.orderStatus}
                      </Badge>
                    </TableCell>
                    <TableCell className="text-right">
                      ₩{order.totalPrice.toLocaleString()}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Card>

          <div className="grid grid-cols-3 gap-6 mt-8">
            <Card className="p-6 flex items-center gap-4 cursor-pointer">
              <Plus className="text-gray-500 w-10 h-10" />
              <div>
                <h4 className="font-semibold">새 상품 등록</h4>
                <p className="text-sm text-gray-500">
                  새로운 상품을 등록하고 관리하세요
                </p>
              </div>
            </Card>

            <Card className="p-6 flex items-center gap-4 cursor-pointer">
              <Truck className="text-gray-500 w-10 h-10" />
              <div>
                <h4 className="font-semibold">상품 관리</h4>
                <p className="text-sm text-gray-500">상품을 관리하세요</p>
              </div>
            </Card>

            <Card className="p-6 flex items-center gap-4 cursor-pointer">
              <ShoppingBag className="text-gray-500 w-10 h-10" />
              <div>
                <h4 className="font-semibold">주문 관리</h4>
                <p className="text-sm text-gray-500">
                  모든 주문을 확인하고 관리하세요
                </p>
              </div>
            </Card>
          </div>
        </div>
      </>
    )
  );
}
