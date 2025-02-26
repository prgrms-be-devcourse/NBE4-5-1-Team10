import RequireAdmin from "@/components/auth/require-admin";
import ClientPage from "./client-page";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";

export default async function Page({
  params,
}: {
  params: {
    id: number;
  };
}) {
  const { id } = await params;
  return (
    <RequireAdmin>
      <ClientPage id={id} />
    </RequireAdmin>
  );
}
